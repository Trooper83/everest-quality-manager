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

    void "create iteration from test case returns iteration"() {
        when:
        def iteration = service.createIterationFromTestCase(testCase)

        then:
        iteration instanceof TestIteration
    }

    void "create iteration from test case returns iteration with test case name"() {
        when:
        def iteration = service.createIterationFromTestCase(testCase)

        then:
        iteration.name == testCase.name
    }

    void "create iteration from test case returns iteration with todo result"() {
        when:
        def iteration = service.createIterationFromTestCase(testCase)

        then:
        iteration.result == "ToDo"
    }

    void "create iteration from test case returns iteration with test case"() {
        when:
        def iteration = service.createIterationFromTestCase(testCase)

        then:
        iteration.testCase == testCase
    }

    void "create iteration from test case returns iteration with steps"() {
        when:
        def iteration = service.createIterationFromTestCase(testCase)

        then:
        iteration.steps.size() == testCase.steps.size()
    }

    void "create iteration from test case returns iteration with steps in order"() {
        when:
        def iteration = service.createIterationFromTestCase(testCase)

        then:
        iteration.steps[0].action == testCase.steps[0].action
        iteration.steps[1].action == testCase.steps[1].action
        iteration.steps[0].result == testCase.steps[0].result
        iteration.steps[1].result == testCase.steps[1].result
    }
}
