package com.everlution.test.testcase

import com.everlution.Person
import com.everlution.Project
import com.everlution.Step
import com.everlution.TestCase
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class TestCaseHibernateSpec extends HibernateSpec {

    @Shared Person person

    def setup() {
        person = new Person(email: "test@test.com", password: "password").save()
    }

    void "test date created auto generated"() {
        when:
        Project project = new Project(name: "Test Case Date Project", code: "TCD").save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        then:
        testCase.dateCreated != null
    }

    void "test save does not cascade to project"() {
        when: "unsaved project is added to test case"
        Project project = new Project(name: "BugServiceSpec Project2", code: "BMP")
        new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete test case does not cascade to project"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZC").save()
        Step step = new Step(action: "first action", result: "first result").save()
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
        Project project = new Project(name: "TestStep Save Project", code: "TPS").save()
        Step testStep = new Step(action: "do something", result: "something happened")
        Step testStep1 = new Step(action: "do something", result: "something happened")
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
        Project project = new Project(name: "TestStep Update Project", code: "TUP").save()
        Step testStep = new Step(action: "do something", result: "something happened")
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep], project: project).save()

        when: "update steps"
        testCase.steps[0].action = "edited action"
        testCase.steps[0].result = "edited result"

        then: "updated steps are retrieved on the step instance"
        def step = Step.findById(testStep.id)
        step.action == "edited action"
        step.result == "edited result"
    }

    void "delete test case cascades to steps"() {
        given: "valid domain instances"
        Project project = new Project(name: "TestStep Cascade Project", code: "TCP").save()
        Step testStep = new Step(action: "do something", result: "something happened")
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
        Step.findById(testStep.id) == null
    }

    void "test steps order persists"() {
        given: "save a test case"
        Project project = new Project(name: "TestStep Cascade Project666", code: "TC1").save()
        Step testStep = new Step(action: "do something", result: "something happened123")
        Step testStep1 = new Step(action: "I did something", result: "something happened231")
        Step testStep2 = new Step(action: "something happened", result: "something happened321")
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

    void "save does not cascade to person"() {
        when: "unsaved person is added to test case"
        Project project = new Project(name: "BugServiceSpec Project2", code: "BMP").save()
        new TestCase(person: new Person(email: "test@test.com", password: "password"), name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }
}
