package com.everlution.test.testrun

import com.everlution.AutomatedTest
import com.everlution.AutomatedTestService
import com.everlution.Project
import com.everlution.TestRun
import com.everlution.TestRunResult
import com.everlution.TestRunService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class TestRunServiceSpec extends Specification implements ServiceUnitTest<TestRunService>, DataTest {

    def setupSpec() {
        mockDomains(TestRun, Project)
    }

    void "returns new with valid instance"() {
        when:
        def t = service.save(new TestRun(name: "name", project: new Project()))

        then:
        t != null
    }

    void "throws validation exception with invalid instance"() {
        when:
        service.save(new TestRun(name: "", project: null))

        then:
        thrown(ValidationException)
    }

    void "get returns instance"() {
        given:
        def t = service.save(new TestRun(name: "name", project: new Project()))

        when:
        def found = service.get(t.id)

        then:
        found != null
    }

    void "get returns null when not found"() {
        when:
        def t = service.get(null)

        then:
        t == null
    }

    void "createAndSave returns empty list when no results passed in"() {
        given:
        def p = new Project(name: "name", code: "nme").save()

        when:
        TestRun t = service.createAndSave("test run name", p, [])

        then:
        t.name == "test run name"
        t.testResults.empty
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
        service.createAndSave("test run", null, [new TestRunResult(testName: "123", result: "Passed")])

        then:
        thrown(ValidationException)
    }

    void "createAndSave with null results returns empty list"() {
        given:
        def p = new Project(name: "name", code: "nme").save()

        when:
        def r = service.createAndSave("test", p, null)

        then:
        r.testResults.empty
        noExceptionThrown()
    }

    void "createAndSave throws validation exception when automated test has errors"() {
        given:
        def p = new Project(name: "name", code: "nme").save()
        service.automatedTestService = Mock(AutomatedTestService) {
            1 * findOrSave(_,_) >> {
                def a = new AutomatedTest()
                throw new ValidationException("error", a.errors)
            }
        }

        when:
        service.createAndSave("test", p, [new TestRunResult(testName: "fullName", result: "Passed")])

        then:
        thrown(ValidationException)
    }
}