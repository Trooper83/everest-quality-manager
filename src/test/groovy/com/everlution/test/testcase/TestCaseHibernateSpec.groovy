package com.everlution.test.testcase

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.Step
import com.everlution.StepTemplate
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestGroup
import com.everlution.TestIteration
import com.everlution.TestResult
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class TestCaseHibernateSpec extends HibernateSpec {

    @Shared Person person
    @Shared Project project

    def setup() {
        person = new Person(email: "test@test.com", password: "!Password2022").save()
        project = new Project(name: "Test Case Date Project", code: "TCD").save()
    }

    void "test dateCreated auto generated"() {
        when:
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        then:
        testCase.dateCreated != null
    }

    void "lastUpdated auto generated"() {
        given:
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        when:
        testCase.name = "new name"
        testCase.save(flush: true)

        then:
        testCase.lastUpdated != null
    }

    void "test save does not cascade to project"() {
        when: "unsaved project is added to test case"
        def proj = new Project(name: "Test Case cascade Project", code: "TCL")
        new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: proj).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete test case does not cascade to project"() {
        given:
        Step step = new Step(act: "first action", result: "first result").save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project, steps: [step]).save()

        when: "delete test case"
        testCase.delete()
        sessionFactory.currentSession.flush()

        then: "project is not deleted"
        TestCase.findById(testCase.id) == null
        Project.findById(project.id) != null
    }

    void "test save case with steps cascades"() {
        given: "unsaved test case and test steps"
        Step testStep = new Step(act: "do something", result: "something happened", person: person, project: project)
        Step testStep1 = new Step(act: "do something", result: "something happened", person: person, project: project)
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep, testStep1], project: project)

        expect:
        testStep.id == null
        testStep1.id == null

        when: "save testcase"
        testCase.save()

        then:
        testStep.id != null
        testStep1.id != null
    }

    void "test update case with steps"() {
        given: "valid test case instance"
        Step testStep = new Step(act: "do something", result: "something happened", person: person, project: project)
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep], project: project).save()

        when: "update steps"
        testCase.steps[0].act = "edited action"
        testCase.steps[0].result = "edited result"

        then: "updated steps are retrieved on the step instance"
        def step = Step.findById(testStep.id)
        step.act == "edited action"
        step.result == "edited result"
    }

    void "delete test case cascades to steps"() {
        given: "valid domain instances"
        Step testStep = new Step(act: "do something", result: "something happened")
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", project: project).addToSteps(testStep)
        testCase.save()

        expect:
        testStep.id != null
        testCase.steps.size() == 1

        when: "delete test case"
        testCase.delete()
        sessionFactory.currentSession.flush()

        then: "steps are not found"
        Step.get(testStep.id) == null
    }

    void "remove from test case deletes steps"() {
        given: "valid domain instances"
        Step testStep = new Step(act: "do something", result: "something happened")
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", project: project).addToSteps(testStep)
        testCase.save()

        expect:
        testStep.id != null
        testCase.steps.size() == 1

        when: "remove steps"
        testCase.removeFromSteps(testStep)
        sessionFactory.currentSession.flush()

        then: "steps are not found"
        testCase.steps.empty
        Step.get(testStep.id) == null
    }

    void "delete test case cascades to steps with template"() {
        given: "valid domain instances"
        def t = new StepTemplate(name: 'template 1', act: 'action jackson', person: person, project: project).save()
        Step testStep = new Step(act: "do something", result: "something happened", template: t)
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", project: project).addToSteps(testStep)
        testCase.save()

        expect:
        t.id != null
        testStep.id != null
        testCase.steps.size() == 1

        when: "delete test case"
        testCase.delete()
        sessionFactory.currentSession.flush()

        then: "steps are not found"
        Step.get(testStep.id) == null
        StepTemplate.get(t.id) != null
    }

    void "remove from test case deletes step with template"() {
        given: "valid domain instances"
        def t = new StepTemplate(name: 'template 1', act: 'action jackson', person: person, project: project).save()
        Step testStep = new Step(act: "do something", result: "something happened", template: t)
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", project: project).addToSteps(testStep)
        testCase.save()

        expect:
        t.id != null
        testStep.id != null
        testCase.steps.size() == 1

        when: "remove steps"
        testCase.removeFromSteps(testStep)
        sessionFactory.currentSession.flush()

        then: "steps are not found"
        testCase.steps.empty
        Step.get(testStep.id) == null
        StepTemplate.get(t.id) != null
    }

    void "test steps order persists"() {
        given: "save a test case"
        Step testStep = new Step(act: "do something", result: "something happened123")
        Step testStep1 = new Step(act: "I did something", result: "something happened231")
        Step testStep2 = new Step(act: "something happened", result: "something happened321")
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", project: project, steps: [testStep, testStep1, testStep2])
        testCase.save()

        when: "get the test case"
        def tc = TestCase.findById(testCase.id)

        then: "step order is the same as when it was created"
        tc.steps[0].id == testStep.id
        tc.steps[1].id == testStep1.id
        tc.steps[2].id == testStep2.id
    }

    void "delete does not cascade to test group"() {
        given: "test case with group"
        TestGroup group = new TestGroup(name: "group", project: project).save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        testCase.addToTestGroups(group)

        expect:
        TestGroup.findById(group.id) != null

        when:
        testCase.delete(flush: true)

        then:
        TestGroup.findById(group.id) != null
        TestCase.findById(testCase.id) == null
    }

    void "removeFrom does not cascade to test group"() {
        given: "test case with group"
        TestGroup group = new TestGroup(name: "group", project: project).save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        group.addToTestCases(testCase)

        expect:
        TestGroup.findById(group.id) != null

        when:
        testCase.removeFromTestGroups(group)

        then:
        TestGroup.findById(group.id) != null
    }

    void "delete test case with iteration"() {
        given:
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def plan = new ReleasePlan(name: "rp123", project: project, status: "ToDo", person: person).save()
        def cycle = new TestCycle(name: "test cycle", releasePlan: plan).save()
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [], testCycle: cycle).save()

        expect:
        TestIteration.findById(iteration.id) != null

        when:
        testCase.delete(flush: true)

        then:
        thrown(DataIntegrityViolationException)
    }
}
