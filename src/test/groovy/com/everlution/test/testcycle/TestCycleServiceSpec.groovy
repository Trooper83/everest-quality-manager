package com.everlution.test.testcycle

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestCycleService
import com.everlution.TestIteration
import com.everlution.TestIterationService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class TestCycleServiceSpec extends Specification implements ServiceUnitTest<TestCycleService>, DataTest {

    def setupSpec() {
        mockDomains(Project, ReleasePlan, TestCycle)
    }

    private void setupData() {
        def project = new Project(name: "release name", code: "doc").save()
        def plan = new ReleasePlan(name: "release plan name", project: project).save()
        new TestCycle(name: "First Test Case", releasePlan: plan).save()
        new TestCycle(name: "Second Test Case", releasePlan: plan).save(flush: true)
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
        def project = new Project(name: "release name", code: "doc").save()
        def plan = new ReleasePlan(name: "release plan name", project: project).save()
        def tc = new TestCycle(name: "First Test Case", releasePlan: plan)

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
        def project = new Project(name: "release name", code: "doc").save()
        def plan = new ReleasePlan(name: "release plan name", project: project).save()
        def person = new Person(email: "test@test.com", password: "test").save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "iteration", testCase: testCase, steps: [], result: "ToDo")
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan).addToTestIterations(iteration).save(flush: true)

        expect:
        tc.testIterations.size() == 1

        when:
        service.removeTestIteration(tc, iteration)

        then:
        tc.testIterations.empty
    }

    void "add test iterations populates property on instance"() {
        service.testIterationService = Mock(TestIterationService) {
            1 * createIterations(_) >> [new TestIteration()]
        }

        given:
        def project = new Project(name: "release name", code: "doc").save()
        def plan = new ReleasePlan(name: "release plan name", project: project).save()
        def person = new Person(email: "test@test.com", password: "test").save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan).save(flush: true)

        expect:
        tc.id != null
        tc.testIterations == null

        when:
        service.addTestIterations(tc, [testCase])

        then:
        tc.testIterations.size() == 1
    }
}
