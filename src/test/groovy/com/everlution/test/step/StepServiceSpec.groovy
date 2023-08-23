package com.everlution.test.step

import com.everlution.Person
import com.everlution.Project
import com.everlution.Step
import com.everlution.StepService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class StepServiceSpec extends Specification implements ServiceUnitTest<StepService>, DataTest {

    def setupSpec() {
        mockDomains(Step, Person, Project)
    }

    @Shared Person person
    @Shared Project project

    private void setupData() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "!Password2022").save()
        new Step(name: 'first name', act: "action", result: "result", person: person, project: project,
                    isBuilderStep: true).save()
        new Step(name: 'second name', act: "action", result: "result", person: person, project: project,
                    isBuilderStep: true).save()
        new Step(act: "action", result: "result", person: person, project: project).save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof Step
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "delete with valid id deletes instance"() {
        given:
        def s = new Step(act: "action", result: "result", person: person, project: project).save(flush: true)

        expect:
        service.get(s.id) != null

        when:
        service.delete(s.id)
        currentSession.flush()

        then:
        service.get(s.id) == null
    }

    void "save with valid object returns instance"() {
        given:
        def step = new Step(act: 'action', result: 'result', person: person, project: project)

        when:
        def saved = service.save(step)

        then:
        saved instanceof Step
    }

    void "save with invalid object throws validation exception"() {
        given:
        def step = new Step()

        when:
        service.save(step)

        then:
        thrown(ValidationException)
    }

    void "find all by project only returns steps with project"() {
        given:
        setupData()
        def proj = new Project(name: "StepServiceSpec Project1223", code: "BP8").save()
        def step = new Step(person: person, act: 'action', result: 'result', project: proj).save(flush: true)

        when:
        def steps = service.findAllByProject(project, [:])

        then:
        steps.count == 2
        steps.results.every { it.project.id == project.id }
        steps.results.every { it.isBuilderStep == true }
        !steps.results.contains(step)
    }

    void "find all by project with null project id returns empty list"() {
        when:
        def steps = service.findAllByProject(null, [:])

        then:
        noExceptionThrown()
        steps.count == 0
        steps.results.size() == 0
    }

    void "find all by name ilike returns test case"(String q) {
        setup:
        setupData()

        expect:
        def steps = service.findAllInProjectByName(project, q, [:])
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
        def steps = service.findAllInProjectByName(project, 'first', [:])

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
        def steps = service.findAllInProjectByName(null, 'test', [:])

        then:
        steps.results.empty
        steps.count == 0
        noExceptionThrown()
    }

    void "find all by name ilike with string"(String s, int size) {
        setup:
        setupData()

        expect:
        def steps = service.findAllInProjectByName(project, s, [:])
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
        setupData()

        expect: "valid instance"
        service.read(1) instanceof Step
    }

    void "read with invalid id returns null"() {
        expect: "invalid instance"
        service.read(99999999) == null
    }
}
