package com.everlution.test.unit.steptemplate

import com.everlution.domains.Link
import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.Relationship
import com.everlution.domains.Step
import com.everlution.domains.StepTemplate
import com.everlution.services.steptemplate.StepTemplateService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class StepTemplateServiceSpec extends Specification implements ServiceUnitTest<StepTemplateService>, DataTest {

    def setupSpec() {
        mockDomains(Person, Project, Link)
    }

    @Shared Person person
    @Shared Project project

    private void setupData() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "!Password2022").save()
        new StepTemplate(name: 'first name', act: "action", result: "result", person: person, project: project).save()
        new StepTemplate(name: 'second name', act: "action", result: "result", person: person, project: project).save()
        new StepTemplate(name: "third name", act: "action", result: "result", person: person, project: project).save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof StepTemplate
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "delete with valid id deletes instance"() {
        given:
        def s = new StepTemplate(act: "action", result: "result", person: person, project: project).save(flush: true)

        expect:
        service.get(s.id) != null

        when:
        service.delete(s.id)
        currentSession.flush()

        then:
        service.get(s.id) == null
    }

    void "delete with invalid id does not throw exception"() {
        when:
        service.delete(null)

        then:
        noExceptionThrown()
    }

    void "save with valid object returns instance"() {
        given:
        def step = new StepTemplate(act: 'action', result: 'result', person: person, project: project)

        when:
        def saved = service.save(step)

        then:
        saved instanceof StepTemplate
    }

    void "findAllInProject only returns templates with project"() {
        given:
        setupData()
        def proj = new Project(name: "StepTemplateServiceSpec Project1223", code: "BP8").save()
        def template = new StepTemplate(name: "n", person: person, act: 'action', result: 'result', project: proj).save(flush: true)

        when:
        def templates = service.findAllInProject(project, [:])

        then:
        templates.count == 3
        templates.results.every { it.project.id == project.id }
        !templates.results.contains(template)
    }

    void "findAllInProject with null project id returns empty list"() {
        when:
        def r = service.findAllInProject(null, [:])

        then:
        noExceptionThrown()
        r.results.empty
    }

    void "findAllInProjectByName returns templates"(String q) {
        setup:
        setupData()

        expect:
        def r = service.findAllInProjectByName(project, q, [:])
        r.results.every { it -> it.name.contains(q.toLowerCase()) }

        where:
        q << ['first', 'fi', 'irs', 't na', 'FIRST']
    }

    void "findAllInProjectByName only returns templates in project"() {
        given:
        setupData()
        def proj = new Project(name: "TestService Spec Project1223", code: "BP8").save()
        def template = new StepTemplate(name:"first", person: person, act: 'action', result: 'result', project: proj).save(flush: true)

        when:
        def r = service.findAllInProjectByName(project, 'first', [:])

        then:
        r.results.every { it.project.id == project.id }
        r.count == 1
        !r.results.contains(template)
    }

    void "findAllInProjectByName with null project"() {
        given:
        setupData()

        when:
        def t = service.findAllInProjectByName(null, 'test', [:])

        then:
        t.results.empty
        noExceptionThrown()
    }

    void "findAllInProjectByName with string"(String s, int size) {
        setup:
        setupData()

        expect:
        def t = service.findAllInProjectByName(project, s, [:])
        t.count == size
        t.results.size() == size

        where:
        s           | size
        null        | 0
        ''          | 3
        'not found' | 0
        'name'      | 3
    }

    void "read returns instance"() {
        setup:
        setupData()

        expect: "valid instance"
        service.read(1) instanceof StepTemplate
    }

    void "read with invalid id returns null"() {
        expect: "invalid instance"
        service.read(null) == null
    }

    void "delete template with links deletes all related links"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name:"name", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "name 1", act: "do something", result: "something happened", person: person, project: project).save()
        def link = new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save(flush:true)

        expect:
        testStepTemplate1.id != null
        testStepTemplate.id != null
        link.id != null

        when:
        service.delete(testStepTemplate.id)

        then:
        service.get(testStepTemplate1.id) != null
        service.get(testStepTemplate.id) == null
        Link.get(link.id) == null
    }

    void "getLinkedTemplatesByRelation returns empty map when template null"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "n", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "n323",act: "do something", result: "something happened", person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save(flush:true)

        when:
        def map = service.getLinkedTemplatesByRelation(null)

        then:
        map.children.empty
        map.parents.empty
        map.siblings.empty
    }

    void "getLinkedTemplatesByRelation returns all items"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "name", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "namedsasd", act: "do something", result: "something happened", person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save(flush:true)
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_PARENT_OF.name).save(flush:true)
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_CHILD_OF.name).save(flush:true)

        when:
        def map = service.getLinkedTemplatesByRelation(testStepTemplate)

        then:
        map.children.size() == 1
        map.parents.size() == 1
        map.siblings.size() == 1
    }

    void "getLinkedTemplatesByRelation returns child links in parents list"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "name", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "namedsasd", act: "do something", result: "something happened", person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_CHILD_OF.name).save(flush:true)

        when:
        def map = service.getLinkedTemplatesByRelation(testStepTemplate)

        then:
        map.children.size() == 0
        map.parents.size() == 1
        map.siblings.size() == 0
    }

    void "getLinkedTemplatesByRelation returns parent links in children list"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "name", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "namedsasd", act: "do something", result: "something happened", person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_PARENT_OF.name).save(flush:true)

        when:
        def map = service.getLinkedTemplatesByRelation(testStepTemplate)

        then:
        map.children.size() == 1
        map.parents.size() == 0
        map.siblings.size() == 0
    }

    void "getLinkedTemplatesByRelation returns sibling links in siblings list"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "name", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "namedsasd", act: "do something", result: "something happened", person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save(flush:true)

        when:
        def map = service.getLinkedTemplatesByRelation(testStepTemplate)

        then:
        map.children.size() == 0
        map.parents.size() == 0
        map.siblings.size() == 1
    }

    void "getLinkedTemplatesByRelation does not return template passed in"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "should not be here", act: "do something", result: "something happened",
                person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "should be returned", act: "do something", result: "something happened",
                person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save()
        new Link(ownerId: testStepTemplate1.id, linkedId: testStepTemplate.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: testStepTemplate1.id, linkedId: testStepTemplate.id, project: project,
                relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: testStepTemplate1.id, linkedId: testStepTemplate.id, project: project,
                relation: Relationship.IS_CHILD_OF.name).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_CHILD_OF.name).save(flush:true)

        when:
        def map = service.getLinkedTemplatesByRelation(testStepTemplate)

        then:
        map.children.size() == 1
        !map.children.contains(testStepTemplate)
        map.parents.size() == 1
        !map.parents.contains(testStepTemplate)
        map.siblings.size() == 1
        !map.siblings.contains(testStepTemplate)
    }

    void "getRelatedTemplates returns related template instance"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "name", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "namedsfdsaf", act: "do something", result: "something happened", person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save(flush:true)
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_PARENT_OF.name).save(flush:true)
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_CHILD_OF.name).save(flush:true)

        when:
        def templates = service.getRelatedTemplates(testStepTemplate.id)

        then:
        templates.relatedStepTemplates.size() == 3
        templates.template.id == testStepTemplate.id
    }

    void "getRelatedTemplates returns empty list and null template for template not found"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "name", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "namedfdsaf", act: "do something", result: "something happened", person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save(flush:true)
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_PARENT_OF.name).save(flush:true)
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_CHILD_OF.name).save(flush:true)

        when:
        def t = service.getRelatedTemplates(9999)

        then:
        t.relatedStepTemplates.size() == 0
        t.template == null
    }

    void "getRelatedTemplates only returns owned related templates"() {
        given:
        setupData()
        StepTemplate testStepTemplate = new StepTemplate(name: "name", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate1 = new StepTemplate(name: "namedwqeqe", act: "do something", result: "something happened", person: person, project: project).save()
        StepTemplate testStepTemplate2 = new StepTemplate(name: "namesadad", act: "do something", result: "something happened", person: person, project: project).save()
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save(flush:true)
        new Link(ownerId: testStepTemplate.id, linkedId: testStepTemplate1.id, project: project,
                relation: Relationship.IS_PARENT_OF.name).save(flush:true)
        new Link(ownerId: testStepTemplate2.id, linkedId: testStepTemplate.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name).save(flush:true)

        when:
        def steps = service.getRelatedTemplates(testStepTemplate.id)

        then:
        steps.relatedStepTemplates.size() == 2
        !steps.relatedStepTemplates.contains(testStepTemplate2)
    }

    void "deleting template with associated step orphans step"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "name", act: "do something", result: "something happened", person: person, project: project).save()
        def s = new Step(act: "testing 12", template: t).save(flush: true)
        def s1 = new Step(act: "testing 12", template: t).save(flush: true)

        expect:
        t.id != null
        s.id != null
        s.template == t
        s1.template == t

        when:
        service.delete(t.id)
        currentSession.flush()

        then:
        noExceptionThrown()
        Step.get(s.id).template == null
        Step.get(s1.id).template == null
    }

    void "updating action on template updates related steps"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "name", act: "first", person: person, project: project).save()
        def s = new Step(act: "first", template: t).save(flush: true)
        def s1 = new Step(act: "first", template: t).save(flush: true)
        def s2 = new Step(act: "first", template: t).save(flush: true)

        expect:
        s.act == "first"
        s.result == null
        s1.act == "first"
        s1.result == null
        s2.act == "first"
        s2.result == null

        when:
        t.act = "action"
        service.update(t, null, null)

        then:
        Step.get(s.id).act == "action"
        Step.get(s.id).result == null
        Step.get(s1.id).act == "action"
        Step.get(s1.id).result == null
        Step.get(s2.id).act == "action"
        Step.get(s2.id).result == null
    }

    void "updating result on template updates related steps"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "name", result: "first", person: person, project: project).save()
        def s = new Step(act: "same", result: "first", template: t).save(flush: true)
        def s1 = new Step(result: "first", template: t).save(flush: true)
        def s2 = new Step(result: "first", template: t).save(flush: true)

        expect:
        s.result == "first"
        s.act == "same"
        s1.result == "first"
        s1.act == null
        s2.result == "first"
        s2.act == null

        when:
        t.result = "res"
        service.update(t, null, null)

        then:
        Step.get(s.id).act == null
        Step.get(s.id).result == "res"
        Step.get(s1.id).act == null
        Step.get(s1.id).result == "res"
        Step.get(s2.id).act == null
        Step.get(s2.id).result == "res"
    }

    void "updating name on template does not update related steps"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "name", result: "test", person: person, project: project).save()
        def s = new Step(act: "same", result: "first", template: t).save(flush: true)
        def s1 = new Step(result: "second", template: t).save(flush: true)
        def s2 = new Step(result: "third", template: t).save(flush: true)

        expect:
        s.result == "first"
        s.act == "same"
        s1.result == "second"
        s1.act == null
        s2.result == "third"
        s2.act == null

        when:
        t.name = "new name"
        service.update(t, null, null)

        then:
        StepTemplate.get(t.id).name == "new name"
        Step.get(s.id).act == "same"
        Step.get(s.id).result == "first"
        Step.get(s1.id).act == null
        Step.get(s1.id).result == "second"
        Step.get(s2.id).act == null
        Step.get(s2.id).result == "third"
    }

    void "update modifies instance"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "name", act: "first", person: person, project: project).save()

        expect:
        t.name == "name"

        when:
        t.name = "new name should be found"
        service.update(t, null, null)

        then:
        service.read(t.id).name == "new name should be found"
    }

    void "update creates links"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "update creates links", act: "first", person: person, project: project).save()
        StepTemplate t1 = new StepTemplate(name: "update creates links part II", act: "second", person: person, project: project).save()
        Link l = new Link(relation: "Is Child of", linkedId: t1.id)

        when:
        service.update(t, [l], [])

        then:
        l.id != null
        l.project == project
        l.ownerId == t.id
    }

    void "update removes links"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "update creates links", act: "first", person: person, project: project).save()
        StepTemplate t1 = new StepTemplate(name: "update creates links part II", act: "second", person: person, project: project).save()
        Link l = new Link(relation: "Is Child of", linkedId: t1.id, project: project, ownerId: t.id).save(flush: true)

        expect:
        service.getRelatedTemplates(t.id).relatedStepTemplates.size() == 1

        when:
        service.update(t, [], [l.id])

        then:
        service.getRelatedTemplates(t.id).relatedStepTemplates.size() == 0
    }

    void "update does not throw exception when linksToAdd and linksToRemove are null"() {
        given:
        setupData()
        StepTemplate t = new StepTemplate(name: "name", act: "first", person: person, project: project).save()

        expect:
        t.name == "name"

        when:
        t.name = "new name should be found"
        service.update(t, null, null)

        then:
        noExceptionThrown()
    }
}