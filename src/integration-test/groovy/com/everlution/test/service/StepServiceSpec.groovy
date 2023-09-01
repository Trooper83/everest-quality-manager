package com.everlution.test.service

import com.everlution.Link
import com.everlution.Person
import com.everlution.Project
import com.everlution.Relationship
import com.everlution.Step
import com.everlution.StepService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class StepServiceSpec extends Specification {

    StepService stepService
    SessionFactory sessionFactory

    @Shared Project project
    @Shared Person person

    private Step setupData() {
        person = new Person(email: "testing@testing321.com", password: "!Password#2022").save()
        project = new Project(name: "testing project", code: "tpt").save()
        new Step(name: 'first name', act: "action", result: "result", person: person, project: project,
                isBuilderStep: true).save()
        def linked = new Step(name: 'second name', act: "action", result: "result", person: person, project: project,
                isBuilderStep: true).save()
        def step = new Step(act: "action1", result: "result1", person: person, project: project).save()
        new Link(ownerId: step.id, linkedId: linked.id, project: project, relation: Relationship.IS_SIBLING_OF.name).save()
        step
    }

    void "get returns step"() {
        setupData()

        expect:
        stepService.get(1) != null
    }

    void "get returns null for not found id"() {
        expect:
        stepService.get(9999999999) == null
    }

    void "delete removes step"() {
        Long stepId = setupData().id

        expect:
        Step.findById(stepId) != null

        when:
        stepService.delete(stepId)
        sessionFactory.currentSession.flush()

        then:
        Step.findById(stepId) == null
    }

    void "save persists step"() {
        setup:
        setupData()

        when:
        Step step = new Step(act: "action", result: "result", person: person, project: project)
        stepService.save(step)

        then:
        step.id != null
    }

    void "save throws validation exception with validation violation"() {
        setup:
        setupData()

        when:
        Step step = new Step(act: "action", result: "result")
        stepService.save(step)

        then:
        thrown(ValidationException)
    }

    void "find all by project only returns steps with project"() {
        given:
        setupData()
        def proj = new Project(name: "StepServiceSpec Project1223", code: "BP8").save()
        def step = new Step(person: person, act: 'action', result: 'result', project: proj).save(flush: true)

        when:
        def steps = stepService.findAllByProject(project, [:])

        then:
        steps.count == 2
        steps.results.every { it.project.id == project.id }
        steps.results.every { it.isBuilderStep == true }
        !steps.results.contains(step)
    }

    void "find all by project with null project id returns empty list"() {
        when:
        def steps = stepService.findAllByProject(null, [:])

        then:
        noExceptionThrown()
        steps.count == 0
        steps.results.size() == 0
    }

    void "find all by name ilike returns test case"(String q) {
        setup:
        setupData()

        expect:
        def steps = stepService.findAllInProjectByName(project, q, [:])
        steps.results.first().name == "first name"

        where:
        q << ['first', 'fi', 'irs', 't na', 'FIRST', 'name']
    }

    void "find all in project by name only returns tests in project"() {
        given:
        setupData()
        def proj = new Project(name: "TestService Spec Project1223", code: "BP8").save()
        def step = new Step(person: person, act: 'ation', result: 'result', project: proj).save(flush: true)

        when:
        def steps = stepService.findAllInProjectByName(project, 'first', [:])

        then:
        steps.results.every { it.project.id == project.id }
        steps.results.every { it.isBuilderStep == true }
        steps.results.size() == 1
        steps.count == 1
        !steps.results.contains(step)
    }

    void "find all in project by name with null project"() {
        given:
        setupData()

        when:
        def steps = stepService.findAllInProjectByName(null, 'test', [:])

        then:
        steps.results.empty
        steps.count == 0
        noExceptionThrown()
    }

    void "find all by name ilike with string"(String s, int size) {
        setup:
        setupData()

        expect:
        def steps = stepService.findAllInProjectByName(project, s, [:])
        steps.results.size() == size
        steps.count == size

        where:
        s           | size
        null        | 0
        ''          | 2
        'not found' | 0
        'name'      | 2
    }

    void "read returns instance"() {
        setup:
        def id = setupData().id

        expect:
        stepService.read(id) instanceof Step
    }

    void "read returns null for not found id"() {
        expect:
        stepService.read(999999999) == null
    }

    void "get steps by relation returns steps"() {
        setup:
        def step = setupData()

        when:
        def steps = stepService.getLinkedStepsByRelation(step)

        then:
        steps.siblings.size() == 1
    }
}
