package com.everlution.test.unit.testiteration

import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.ReleasePlan
import com.everlution.domains.TestCase
import com.everlution.domains.TestCycle
import com.everlution.domains.TestIteration
import com.everlution.services.testiteration.TestIterationService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
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
        person = new Person(email: "test@test.com", password: "!Password2022").save()
        project = new Project(name: "tc domain project", code: "tdp").save()
        testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
    }

    void "get with valid id returns instance"() {
        when:
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        new TestIteration(name: "name", testCase: testCase, steps: [], result: "ToDo", testCycle: testCycle).save()

        then:
        service.get(1) instanceof TestIteration
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "read with valid id returns instance"() {
        when:
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        new TestIteration(name: "name", testCase: testCase, steps: [], result: "ToDo", testCycle: testCycle).save()

        then:
        service.read(1) instanceof TestIteration
    }

    void "read with invalid id returns null"() {
        expect:
        service.read(999999) == null
    }

    void "save with valid iteration returns instance"() {
        given:
        def person = new Person(email: "test@test.com", password: "!Password2022").save()
        def project = new Project(name: "tc domain project", code: "tdp").save()
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [],
                testCycle: testCycle)

        when:
        def saved = service.save(iteration)

        then:
        saved instanceof TestIteration
    }

    void "save with invalid iteration throws validation exception"() {
        given:
        def iteration = new TestIteration()

        when:
        service.save(iteration)

        then:
        thrown(ValidationException)
    }

    void "findAllByTestCycle returns iterations"() {
        given:
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [],
                testCycle: testCycle)
        service.save(iteration)

        when:
        def found = service.findAllByTestCycle(testCycle, [:])

        then:
        found.size() == 1
        found.first() == iteration
    }

    void "findAllByTestCycle null id returns empty list"() {
        when:
        def l = service.findAllByTestCycle(null, [:])

        then:
        noExceptionThrown()
        l.empty
    }

    void "findAllByTestCycle null params returns empty list"() {
        when:
        def l = service.findAllByTestCycle(new TestCycle(), null)

        then:
        noExceptionThrown()
        l.empty
    }

    void "findAllByTestCycle correctly sorts ascending when order and sort args present"() {
        given:
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "1test name", testCase: testCase, result: "ToDo", steps: [],
                testCycle: testCycle)
        def iteration1 = new TestIteration(name: "2test name", testCase: testCase, result: "ToDo", steps: [],
                testCycle: testCycle)
        service.save(iteration)
        service.save(iteration1)

        when:
        def found = service.findAllByTestCycle(testCycle, [sort:'name', order:'asc'])

        then:
        found[0].name == '1test name'
        found[1].name == '2test name'
    }

    void "findAllByTestCycle correctly sorts descending when order and sort args present"() {
        given:
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "1test name", testCase: testCase, result: "ToDo", steps: [],
                testCycle: testCycle)
        def iteration1 = new TestIteration(name: "2test name", testCase: testCase, result: "ToDo", steps: [],
                testCycle: testCycle)
        service.save(iteration)
        service.save(iteration1)

        when:
        def found = service.findAllByTestCycle(testCycle, [sort:'name', order:'desc'])

        then:
        found[0].name == '2test name'
        found[1].name == '1test name'
    }
}
