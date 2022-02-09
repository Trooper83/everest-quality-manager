package com.everlution.test.testiteration

import com.everlution.Person
import com.everlution.Project
import com.everlution.Step
import com.everlution.TestCase
import com.everlution.TestIteration
import com.everlution.TestIterationService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestIterationServiceSpec extends Specification implements ServiceUnitTest<TestIterationService>, DataTest {

    @Shared Person person
    @Shared Project project
    @Shared TestCase testCase

    def setupSpec() {
        mockDomains(Person, Project, TestCase, TestIteration)
    }

    def setup() {
        person = new Person(email: "test@test.com", password: "test").save()
        project = new Project(name: "tc domain project", code: "tdp").save()
        def step = new Step(action: "action", result: "result")
        def step1 = new Step(action: "action1", result: "result1")
        testCase = new TestCase(person: person, name: "First Test Case", project: project, steps: [step, step1]).save()
    }

    void "get with valid parameter returns instance"() {
        when:
        new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: []).save()

        then:
        service.get(1) != null
    }

    void "get with invalid instance returns null"() {
        expect:
        service.get(null) == null
    }

    void "create iterations from test cases returns iteration"() {
        when:
        def iterations = service.createIterations([testCase])

        then:
        iterations.first() instanceof TestIteration
    }

    void "create iterations from test cases returns iterations with test case name"() {
        when:
        def iterations = service.createIterations([testCase])

        then:
        iterations.first().name == testCase.name
    }

    void "create iterations from test cases returns iteration with todo result"() {
        when:
        def iterations = service.createIterations([testCase])

        then:
        iterations.first().result == "ToDo"
    }

    void "create iterations from test cases returns iteration with test case"() {
        when:
        def iterations = service.createIterations([testCase])

        then:
        iterations.first().testCase == testCase
    }

    void "create iteration from test case returns iteration with steps"() {
        when:
        def iterations = service.createIterations([testCase])

        then:
        iterations.first().steps.size() == testCase.steps.size()
    }

    void "create iterations from test cases returns iterations with steps in order"() {
        when:
        def iterations = service.createIterations([testCase])

        then:
        iterations.first().steps[0].action == testCase.steps[0].action
        iterations.first().steps[1].action == testCase.steps[1].action
        iterations.first().steps[0].result == testCase.steps[0].result
        iterations.first().steps[1].result == testCase.steps[1].result
    }

    void "create iterations from test cases returns same number of tests cases"() {
        when:
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project).save()
        def iterations = service.createIterations([testCase, testCase1])

        then:
        iterations.size() == 2
    }

    void "create iterations with empty test case list does not throw exception"() {
        when:
        service.createIterations([])

        then:
        noExceptionThrown()
    }

    void "create iterations with null does not throw exception"() {
        when:
        def iterations = service.createIterations(null)

        then:
        noExceptionThrown()
        iterations.empty
    }

    void "create iterations with null steps on test case does not throw exception"() {
        given:
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project).save()

        expect:
        testCase1.steps == null

        when:
        service.createIterations([testCase1])

        then:
        noExceptionThrown()
    }
}
