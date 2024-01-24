package com.everlution.test.testresult

import com.everlution.AutomatedTest
import com.everlution.Project
import com.everlution.TestResult
import com.everlution.TestResultService
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
}
