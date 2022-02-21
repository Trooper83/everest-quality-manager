package com.everlution.test.testcycle

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.Step
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestCycleService
import com.everlution.TestIteration
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class TestCycleServiceSpec extends Specification implements ServiceUnitTest<TestCycleService>, DataTest {

    @Shared ReleasePlan releasePlan
    @Shared Person person
    @Shared Project project

    def setupSpec() {
        mockDomains(Project, ReleasePlan, TestCycle)
    }

    def setup() {
        project = new Project(name: "release name", code: "doc").save()
        person = new Person(email: "test@test.com", password: "test").save()
        releasePlan = new ReleasePlan(name: "release plan name", project: project).save()
    }

    private void setupData() {
        new TestCycle(name: "First Test Case", releasePlan: releasePlan).save()
        new TestCycle(name: "Second Test Case", releasePlan: releasePlan).save(flush: true)
    }

    void "get returns valid instance"() {
        setupData()

        expect:
        service.get(1) instanceof TestCycle
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "get with null returns null"() {
        expect:
        service.get(null) == null
    }

    void "save with valid object returns instance"() {
        given:
        def tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan)

        when:
        def saved = service.save(tc)

        then:
        saved instanceof TestCycle
    }

    void "save with invalid object throws validation exception"() {
        given:
        TestCycle tc = new TestCycle()

        when:
        service.save(tc)

        then:
        thrown(ValidationException)
    }

    void "removeFrom removes iterations"() {
        given:
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "iteration", testCase: testCase, steps: [], result: "ToDo")
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan).addToTestIterations(iteration).save(flush: true)

        expect:
        tc.testIterations.size() == 1

        when:
        service.removeTestIteration(tc, iteration)

        then:
        tc.testIterations.empty
    }

    void "add test iterations populates property on instance"() {
        given:
        def step = new Step(action: "action", result: "result")
        def step1 = new Step(action: "action1", result: "result1")
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project, steps: [step, step1]).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan).save(flush: true)

        expect:
        tc.id != null
        tc.testIterations == null

        when:
        service.addTestIterations(tc, [testCase])

        then:
        tc.testIterations.size() == 1
        tc.testIterations.first().name == testCase.name
        tc.testIterations.first().result == "ToDo"
        tc.testIterations.first().testCase == testCase
        tc.testIterations.first().steps.size() == testCase.steps.size()
        tc.testIterations.first().steps[0].action == testCase.steps[0].action
        tc.testIterations.first().steps[1].action == testCase.steps[1].action
        tc.testIterations.first().steps[0].result == testCase.steps[0].result
        tc.testIterations.first().steps[1].result == testCase.steps[1].result
    }

    void "create iterations from test cases returns same number of tests cases"() {
        when:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project).save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan).save(flush: true)
        service.addTestIterations(tc, [testCase, testCase1])

        then:
        tc.testIterations.size() == 2
    }

    void "create iterations with empty test case list does not throw exception"() {
        when:
        service.addTestIterations(new TestCycle(), [])

        then:
        noExceptionThrown()
    }

    void "create iterations with null does not throw exception"() {
        when:
        service.addTestIterations(new TestCycle(), null)

        then:
        noExceptionThrown()
    }

    void "create iterations with null steps on test case does not throw exception"() {
        given:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project).save()

        expect:
        testCase.steps == null

        when:
        service.addTestIterations(new TestCycle(), [testCase])

        then:
        noExceptionThrown()
    }
}
