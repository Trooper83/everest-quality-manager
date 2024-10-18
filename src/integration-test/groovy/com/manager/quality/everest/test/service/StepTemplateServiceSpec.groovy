package com.manager.quality.everest.test.service

import com.manager.quality.everest.domains.Link
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.Relationship
import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.domains.StepTemplate
import com.manager.quality.everest.services.steptemplate.StepTemplateService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import org.hibernate.SessionFactory
import spock.lang.Shared
import spock.lang.Specification

@Rollback
@Integration
class StepTemplateServiceSpec extends Specification {

    StepTemplateService stepTemplateService
    SessionFactory sessionFactory

    @Shared Project project
    @Shared Person person

    private StepTemplate setupData() {
        person = new Person(email: "testing@testing3214545234.com", password: "!Password#2022").save()
        project = new Project(name: "testing project235435353424", code: "tp6").save()
        new StepTemplate(name: 'first name', act: "action", result: "result", person: person, project: project).save()
        def linked = new StepTemplate(name: 'second name', act: "action", result: "result", person: person, project: project).save()
        def template = new StepTemplate(name: 'another name', act: "action1", result: "result1", person: person, project: project).save()
        new Link(ownerId: template.id, linkedId: linked.id, project: project, relation: Relationship.IS_SIBLING_OF.name).save()
        template
    }

    void "get returns template"() {
        def t = setupData()

        expect:
        stepTemplateService.get(t.id) != null
    }

    void "get returns null for not found id"() {
        expect:
        stepTemplateService.get(9999999999) == null
    }

    void "delete removes step"() {
        Long templateId = setupData().id

        expect:
        StepTemplate.get(templateId) != null

        when:
        stepTemplateService.delete(templateId)
        sessionFactory.currentSession.flush()

        then:
        StepTemplate.get(templateId) == null
    }

    void "delete does not thrown exception when invalid id"() {
        when:
        stepTemplateService.delete(null)
        sessionFactory.currentSession.flush()

        then:
        noExceptionThrown()
    }

    void "save persists template"() {
        setup:
        setupData()

        when:
        def template = new StepTemplate(name: 'name', act: "action", result: "result", person: person, project: project)
        stepTemplateService.save(template)

        then:
        template.id != null
    }

    void "save throws validation exception with validation violation"() {
        setup:
        setupData()

        when:
        def template = new StepTemplate(act: "action", result: "result", person: person, project: project)
        stepTemplateService.save(template)

        then:
        thrown(ValidationException)
    }

    void "find all by project only returns template in project"() {
        given:
        setupData()
        def proj = new Project(name: "StepServiceSpec Project1223", code: "BP8").save()
        def t = new StepTemplate(name: 'name', person: person, act: 'action', result: 'result', project: proj).save(flush: true)

        when:
        def templates = stepTemplateService.findAllInProject(project, [:])

        then:
        templates.count == 3
        templates.results.every { it.project.id == project.id }
        !templates.results.contains(t)
    }

    void "findAllInProject with null project id returns empty list"() {
        when:
        def templates = stepTemplateService.findAllInProject(null, [:])

        then:
        noExceptionThrown()
        templates.count == 0
        templates.results.size() == 0
    }

    void "findAllInProjectByName returns test case"(String q) {
        setup:
        setupData()

        expect:
        def templates = stepTemplateService.findAllInProjectByName(project, q, [:])
        templates.results.first().name == "first name"

        where:
        q << ['first', 'fi', 'irs', 't na', 'FIRST', 'name']
    }

    void "findAllInProjectByName only returns tests in project"() {
        given:
        setupData()
        def proj = new Project(name: "TestService Spec Project1223", code: "BP8").save()
        def t = new StepTemplate(name: 'first', person: person, act: 'action', result: 'result',
                project: proj).save(flush: true)

        when:
        def templates = stepTemplateService.findAllInProjectByName(project, 'first', [:])

        then:
        templates.results.every { it.project.id == project.id }
        templates.results.size() == 1
        templates.count == 1
        !templates.results.contains(t)
    }

    void "findAllInProjectByName with null project"() {
        given:
        setupData()

        when:
        def templates = stepTemplateService.findAllInProjectByName(null, 'test', [:])

        then:
        templates.results.empty
        templates.count == 0
        noExceptionThrown()
    }

    void "findAllInProjectByName with string"(String s, int size) {
        setup:
        setupData()

        expect:
        def templates = stepTemplateService.findAllInProjectByName(project, s, [:])
        templates.results.size() == size
        templates.count == size

        where:
        s           | size
        null        | 0
        ''          | 3
        'not found' | 0
        'name'      | 3
    }

    void "read returns instance"() {
        setup:
        def id = setupData().id

        expect:
        stepTemplateService.read(id) instanceof StepTemplate
    }

    void "read returns null for not found id"() {
        expect:
        stepTemplateService.read(999999999) == null
    }

    void "getLinkedTemplatesByRelation returns templates"() {
        setup:
        def template = setupData()

        when:
        def templates = stepTemplateService.getLinkedTemplatesByRelation(template)

        then:
        templates.siblings.size() == 1
    }

    void "getRelatedTemplates returns templates"() {
        setup:
        def t = setupData()

        when:
        def templates = stepTemplateService.getRelatedTemplates(t.id)

        then:
        templates.template.id == t.id
        templates.relatedStepTemplates.size() == 1
    }

    void "update does not modify steps when validation exception thrown"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "name", act: "first", person: person, project: project)
        stepTemplateService.save(t)
        def s = new Step(act: "same same", result: "result", template: t).save(flush: true)

        expect:
        t.id != null
        s.act == "same same"
        s.result == "result"

        when:
        t.name = null
        t.act = null
        stepTemplateService.update(t, null, null)

        then:
        thrown(ValidationException)
        Step.get(s.id).act == "same same"
        Step.get(s.id).result == "result"
    }

    void "update throws validation exception when link fails validation"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "name for validation fail for link", act: "first", person: person, project: project)
        stepTemplateService.save(t)

        when:
        stepTemplateService.update(t, [new Link()], null)

        then:
        thrown(ValidationException)
    }
}