package com.everlution.test.testresult

import com.everlution.Person
import com.everlution.Project
import com.everlution.TestCase
import com.everlution.TestResult
import com.everlution.TestResultService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class TestResultServiceSpec extends Specification implements ServiceUnitTest<TestResultService>, DataTest {

    @Shared Person person
    @Shared Project project
    @Shared TestCase testCase

    def setupSpec() {
        mockDomains(TestCase, Person, Project, TestResult)
    }

    private void setupData() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "!Password2022").save()
        testCase = new TestCase(person: person, project: project, name: "name of the test").save()
    }

    void "delete with valid id deletes instance"() {
        given:
        setupData()
        def tr = new TestResult(testCase: testCase, result: "Passed").save(flush: true)

        expect:
        tr.id != null

        when:
        service.delete(tr.id)

        then:
        TestResult.findById(tr.id) == null
    }

    void "delete with invalid id does not throw an exception"() {
        when:
        service.delete(999)

        then:
        noExceptionThrown()
    }

    void "save with valid object returns instance"() {
        given:
        def tr = new TestResult(testCase: testCase, result: "Passed")

        when:
        def saved = service.save(tr)

        then:
        saved instanceof TestResult
    }

    void "save with invalid object throws validation exception"() {
        given:
        def tr = new TestResult()

        when:
        service.save(tr)

        then:
        thrown(ValidationException)
    }

    void "findAllByTestCase returns all test results related to test case"() {
        given:
        setupData()
        def tc = new TestCase(person: person, project: project, name: "what's my name").save()
        def notFound = new TestResult(testCase: tc, result: "Failed").save()
        def tr = new TestResult(testCase: testCase, result: "Skipped").save()
        def tr1 = new TestResult(testCase: testCase, result: "Passed").save(flush: true)

        when:
        def results = service.findAllByTestCase(testCase)

        then:
        results.size() == 2
        results.containsAll(tr, tr1)
        !results.contains(notFound)
    }

    void "findAllByTestCase with null testCase returns empty list"() {
        when:
        def results = service.findAllByTestCase(null)

        then:
        results.empty
        noExceptionThrown()
    }

    void "deleteAllByTestCase deletes all results related to test case"() {
        given:
        setupData()
        def r = new TestResult(result: "Failed", testCase: testCase).save()
        def r1 = new TestResult(result: "Failed", testCase: testCase).save(flush: true)

        expect:
        TestResult.get(r.id) != null
        TestResult.get(r1.id) != null

        when:
        service.deleteAllByTestCase(testCase)

        then:
        TestResult.get(r.id) == null
        TestResult.get(r1.id) == null
    }

    void "deleteAllByTestCase does not throw exception when testCase is null"() {
        when:
        service.deleteAllByTestCase(null)

        then:
        noExceptionThrown()
    }
}
