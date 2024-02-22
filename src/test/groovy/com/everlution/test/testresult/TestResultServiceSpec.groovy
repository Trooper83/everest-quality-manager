package com.everlution.test.testresult

import com.everlution.AutomatedTest
import com.everlution.Project
import com.everlution.TestResult
import com.everlution.TestResultService
import com.everlution.TestRun
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
        def t = new TestRun(name: "test run", project: p).save()
        def tr = new TestResult(automatedTest: at, result: "Passed", testRun: t).save()
        currentSession.flush()

        when:
        def r = service.findAllByAutomatedTest(at)

        then:
        r.contains(tr)
    }

    void "getResultsForAutomatedTest does not throw exception when null"() {
        when:
        service.getResultsForAutomatedTest(null)

        then:
        noExceptionThrown()
    }

    void "getResultsForAutomatedTest sets zero for all numbers when no results found"() {
        when:
        def a = service.getResultsForAutomatedTest(null)

        then:
        a.total == 0
        a.passTotal == 0
        a.failTotal == 0
        a.skipTotal == 0
        a.recentPassTotal == 0
        a.recentFailTotal == 0
        a.recentSkipTotal == 0
        a.recentResults.empty
    }

    void "getResultsForAutomatedTest returns correct numbers"() {
        given:
        def p = new Project(name: 'testing results999', code: 'tr9').save()
        def at = new AutomatedTest(project: p, fullName: 'full name999').save()
        def t = new TestRun(name: "test run", project: p).save()
        def tr = new TestResult(automatedTest: at, result: "Passed", testRun: t).save()
        def tr1 = new TestResult(automatedTest: at, result: "Failed", testRun: t).save()
        def tr2 = new TestResult(automatedTest: at, result: "Skipped", testRun: t).save()
        def tr3 = new TestResult(automatedTest: at, result: "Passed", testRun: t).save()
        def tr4 = new TestResult(automatedTest: at, result: "Failed", testRun: t).save()
        def tr5 = new TestResult(automatedTest: at, result: "Skipped", testRun: t).save(flush: true)

        when:
        def found = service.getResultsForAutomatedTest(at)

        then:
        found.recentResults.containsAll([tr,tr1,tr2,tr3,tr4,tr5])
        found.total == 6
        found.passTotal == 2
        found.failTotal == 2
        found.skipTotal == 2
        found.recentPassTotal == 2
        found.recentFailTotal == 2
        found.recentSkipTotal == 2
    }

    void "getResultsForAutomatedTest returns correct numbers when more than 20 results found"() {
        given:
        def p = new Project(name: 'testing results9999999', code: 'tr999').save()
        def at = new AutomatedTest(project: p, fullName: 'full name999').save()
        def t = new TestRun(project: p, name: "test run name").save()
        int i = 0
        while(i < 4) {
            new TestResult(automatedTest: at, result: "Passed", testRun: t).save()
            new TestResult(automatedTest: at, result: "Passed", testRun: t).save()
            new TestResult(automatedTest: at, result: "Passed", testRun: t).save()
            new TestResult(automatedTest: at, result: "Failed", testRun: t).save()
            new TestResult(automatedTest: at, result: "Skipped", testRun: t).save()
            i++
        }
        currentSession.flush()
        int j = 0
        def expected = []
        def first = new TestResult(automatedTest: at, result: "Skipped", testRun: t).save()
        while(j < 4) {
            expected.add(new TestResult(automatedTest: at, result: "Passed", testRun: t).save())
            expected.add(new TestResult(automatedTest: at, result: "Failed", testRun: t).save())
            expected.add(new TestResult(automatedTest: at, result: "Failed", testRun: t).save())
            expected.add(new TestResult(automatedTest: at, result: "Skipped", testRun: t).save())
            expected.add(new TestResult(automatedTest: at, result: "Skipped", testRun: t).save())
            j++
        }
        currentSession.flush()

        when:
        def found = service.getResultsForAutomatedTest(at)

        then:
        found.recentResults.containsAll(expected)
        !found.recentResults.contains(first)
        found.recentResults.size() == 20
        found.total == 41
        found.passTotal == 16
        found.failTotal == 12
        found.skipTotal == 13
        found.recentPassTotal == 4
        found.recentFailTotal == 8
        found.recentSkipTotal == 8
    }
}
