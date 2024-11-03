package com.manager.quality.everest.test.unit.testresult

import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestResult
import com.manager.quality.everest.services.testresult.TestResultService
import com.manager.quality.everest.domains.TestRun
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
        def tr = new TestResult(automatedTest: at, result: "PASSED", testRun: t).save()
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
        def tr = new TestResult(automatedTest: at, result: "PASSED", testRun: t).save()
        def tr1 = new TestResult(automatedTest: at, result: "FAILED", testRun: t).save()
        def tr2 = new TestResult(automatedTest: at, result: "SKIPPED", testRun: t).save()
        def tr3 = new TestResult(automatedTest: at, result: "PASSED", testRun: t).save()
        def tr4 = new TestResult(automatedTest: at, result: "FAILED", testRun: t).save()
        def tr5 = new TestResult(automatedTest: at, result: "SKIPPED", testRun: t).save(flush: true)

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
            new TestResult(automatedTest: at, result: "PASSED", testRun: t).save()
            new TestResult(automatedTest: at, result: "PASSED", testRun: t).save()
            new TestResult(automatedTest: at, result: "PASSED", testRun: t).save()
            new TestResult(automatedTest: at, result: "FAILED", testRun: t).save()
            new TestResult(automatedTest: at, result: "SKIPPED", testRun: t).save()
            i++
        }
        currentSession.flush()
        int j = 0
        def expected = []
        def first = new TestResult(automatedTest: at, result: "SKIPPED", testRun: t).save()
        while(j < 4) {
            expected.add(new TestResult(automatedTest: at, result: "PASSED", testRun: t).save())
            expected.add(new TestResult(automatedTest: at, result: "FAILED", testRun: t).save())
            expected.add(new TestResult(automatedTest: at, result: "FAILED", testRun: t).save())
            expected.add(new TestResult(automatedTest: at, result: "SKIPPED", testRun: t).save())
            expected.add(new TestResult(automatedTest: at, result: "SKIPPED", testRun: t).save())
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

    void "findAllByTestRun returns results"() {
        given:
        def p = new Project(name: "project name", code: "pcdr1").save()
        def t = new TestRun(project: p, name: "testrun name").save()
        def a = new AutomatedTest(project: p, fullName: "this is my full name").save()
        def r = new TestResult(result: "PASSED", automatedTest: a, testRun: t).save(flush: true)

        when:
        def found = service.findAllByTestRun(t, [:])

        then:
        found.contains(r)
    }

    void "findAllByTestRun returns empty list when none found"() {
        when:
        def r = service.findAllByTestRun(null, [:])

        then:
        r.empty
        noExceptionThrown()
    }
}
