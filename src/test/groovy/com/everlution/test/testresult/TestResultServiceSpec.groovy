package com.everlution.test.testresult

import com.everlution.AutomatedTest
import com.everlution.AutomatedTestService
import com.everlution.Project
import com.everlution.TestResult
import com.everlution.TestResultService
import com.everlution.TestRunResult
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class TestResultServiceSpec extends Specification implements ServiceUnitTest<TestResultService>, DataTest {

    def setupSpec() {
        mockDomains(AutomatedTest, TestResult, Project)
    }

    void "save with valid object returns instance"() {
        given:
        def p = new Project(name: 'testing results', code: 'trs')
        def at = new AutomatedTest(project: p, fullName: 'full name')
        def tr = new TestResult(automatedTest: at, result: "Passed")

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

    void "createAndSave returns empty list when no results passed in"() {
        given:
        def p = new Project(name: "name", code: "nme").save()

        when:
        def r = service.createAndSave(p, [])

        then:
        r.empty
    }

    void "createAndSave throws validation exception with null project"() {
        given:
        service.automatedTestService = Mock(AutomatedTestService) {
            1 * findOrSave(_,_) >> {
                def a = new AutomatedTest()
                throw new ValidationException("Invalid", a.errors)
            }
        }
        when:
        service.createAndSave(null, [new TestRunResult(testName: "123", result: "Passed")])

        then:
        thrown(ValidationException)
    }

    void "createAndSave with null results returns empty list"() {
        given:
        def p = new Project(name: "name", code: "nme").save()

        when:
        def r = service.createAndSave(p, null)

        then:
        r.empty
    }

    void "createAndSave returns test result with properties populated"() {
        given:
        def p = new Project(name: "name", code: "nme").save()
        def a = new AutomatedTest(project: p, fullName: "fullName").save()
        service.automatedTestService = Mock(AutomatedTestService) {
            1 * findOrSave(_,_) >> a
        }

        when:
        def results = service.createAndSave(p, [new TestRunResult(testName: "fullName", result: "Passed")])

        then:
        results.first().result == "Passed"
        results.first().automatedTest == a
    }

    void "createAndSave throws validation exception when automated test has errors"() {
        given:
        def p = new Project(name: "name", code: "nme").save()
        service.automatedTestService = Mock(AutomatedTestService) {
            1 * findOrSave(_,_) >> null
        }

        when:
        service.createAndSave(p, [new TestRunResult(testName: "fullName", result: "Passed")])

        then:
        thrown(ValidationException)
    }

    void "createAndSave throws validation exception when result has errors"() {
        given:
        def p = new Project(name: "name", code: "nme").save()
        service.automatedTestService = Mock(AutomatedTestService) {
            1 * findOrSave(_,_) >> new AutomatedTest(project: p, fullName: "fullname 123")
        }

        when:
        service.createAndSave(p, [new TestRunResult(testName: "fullName", result: "should fail")])

        then:
        thrown(ValidationException)
    }
}
