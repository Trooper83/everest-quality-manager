package com.everlution.test.testresult

import com.everlution.AutomatedTest
import com.everlution.Project
import com.everlution.TestResult
import com.everlution.TestResultService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class TestResultServiceSpec extends Specification implements ServiceUnitTest<TestResultService>, DataTest {

    def setupSpec() {
        mockDomains(AutomatedTest, TestResult, Project)
    }

    void "findAllByAutomatedTest returns empty list when none found"() {
        when:
        def r = service.findAllByAutomatedTest(null)

        then:
        r.empty
        noExceptionThrown()
    }

    void "findAllByAutomatedTest returns results when found"() {
        given:
        def p = new Project(name: 'testing results999', code: 'tr9').save()
        def at = new AutomatedTest(project: p, fullName: 'full name999').save()
        def tr = new TestResult(automatedTest: at, result: "Passed").save()
        currentSession.flush()

        when:
        def r = service.findAllByAutomatedTest(at)

        then:
        r.contains(tr)
    }
}
