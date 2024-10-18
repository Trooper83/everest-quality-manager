package com.manager.quality.everest.test.service

import com.manager.quality.everest.services.automatedtest.AutomatedTestService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.TestResult
import com.manager.quality.everest.services.testresult.TestResultService
import com.manager.quality.everest.domains.TestRun
import com.manager.quality.everest.services.testrun.TestRunService
import com.manager.quality.everest.test.support.DataFactory
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class TestResultServiceSpec extends Specification {

    AutomatedTestService automatedTestService
    ProjectService projectService
    TestResultService testResultService
    TestRunService testRunService

    void "findAllByAutomatedTest returns results"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "create this or find one")
        def t = new TestResult(automatedTest: a, result: "PASSED")
        testRunService.save(new TestRun(name: "name", project: p, testResults: [t]))

        when:
        def results = testResultService.findAllByAutomatedTest(a)

        then:
        results.contains(t)
    }

    void "getResultsForAutomatedTest returns results"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "create this or find one")
        def t = new TestResult(automatedTest: a, result: "PASSED")
        def t1 = new TestResult(automatedTest: a, result: "FAILED")
        def t2 = new TestResult(automatedTest: a, result: "SKIPPED")
        testRunService.save(new TestRun(name: "name", project: p, testResults: [t,t1,t2]))

        when:
        def results = testResultService.getResultsForAutomatedTest(a)

        then:
        results.recentResults.containsAll([t,t1,t2])
        results.recentResults.size() == 3
        results.total == 3
        results.passTotal == 1
        results.failTotal == 1
        results.skipTotal == 1
        results.recentPassTotal == 1
        results.recentFailTotal == 1
        results.recentSkipTotal == 1
    }

    void "findAllByTestRun returns results"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "create this or find one")
        def t = new TestResult(automatedTest: a, result: "PASSED")
        def r = testRunService.save(new TestRun(name: "name", project: p, testResults: [t]))

        when:
        def results = testResultService.findAllByTestRun(r, [:])

        then:
        results.contains(t)
    }

    void "getMostFailedTestCount returns list count of failed only"() {
        given:
        def p = DataFactory.createProject()
        def a = automatedTestService.findOrSave(p, "create this or find one")
        def t = new TestResult(automatedTest: a, result: "PASSED")
        def t1 = new TestResult(automatedTest: a, result: "FAILED")
        def t3 = new TestResult(automatedTest: a, result: "FAILED")
        def t2 = new TestResult(automatedTest: a, result: "SKIPPED")
        testRunService.save(new TestRun(name: "name", project: p, testResults: [t,t1,t2,t3]))

        when:
        def results = testResultService.getMostFailedTestCount(p)

        then:
        results.size() == 1
        results.first()[0] == a.id
        results.first()[1] == a.fullName
        results.first()[2] == 2
    }

    void "getMostFailedTestCount returns empty when no results found"() {
        when:
        def p = DataFactory.createProject()
        def results = testResultService.getMostFailedTestCount(p)

        then:
        results.empty
        noExceptionThrown()
    }

    void "getMostFailedTestCount returns empty when project null"() {
        when:
        def results = testResultService.getMostFailedTestCount(null)

        then:
        results.empty
        noExceptionThrown()
    }

    void "getMostFailedTestCount returns counts within project only"() {
        given:
        def p = DataFactory.createProject()
        def a = automatedTestService.findOrSave(p, "create this or find one")
        def t = new TestResult(automatedTest: a, result: "FAILED")
        testRunService.save(new TestRun(name: "name", project: p, testResults: [t]))
        def p1 = projectService.list(max:1).first()
        def a1 = automatedTestService.findOrSave(p1, "should not be found")
        def t1 = new TestResult(automatedTest: a1, result: "FAILED")
        testRunService.save(new TestRun(name: "name of the second", project: p, testResults: [t1]))

        when:
        def results = testResultService.getMostFailedTestCount(p)

        then:
        results.first()[0] == a.id
        results.size() == 1
    }

    void "getMostFailedTestCount returns counts in descending order"() {
        given:
        def p = DataFactory.createProject()
        def a = automatedTestService.findOrSave(p, "create this or find one")
        def a1 = automatedTestService.findOrSave(p, "create this or find one1")
        def t = new TestResult(automatedTest: a, result: "FAILED")
        def t1 = new TestResult(automatedTest: a1, result: "FAILED")
        def t2 = new TestResult(automatedTest: a1, result: "FAILED")
        testRunService.save(new TestRun(name: "name", project: p, testResults: [t, t1, t2]))
        def at1 = automatedTestService.findOrSave(p, "should not be found")
        def at2 = automatedTestService.findOrSave(p, "should not be found1")
        def tr1 = new TestResult(automatedTest: at1, result: "FAILED")
        def tr2 = new TestResult(automatedTest: at1, result: "FAILED")
        def tr3 = new TestResult(automatedTest: at1, result: "FAILED")
        def tr4 = new TestResult(automatedTest: at2, result: "FAILED")
        testRunService.save(new TestRun(name: "name of the second", project: p, testResults: [tr1, tr2, tr3, tr4]))

        when:
        def results = testResultService.getMostFailedTestCount(p)

        then:
        results[0][2] == 3
        results[1][2] == 2
        results[2][2] == 1
        results[3][2] == 1
    }

    void "getMostFailedTestCount returns list of 10"() {
        given:
        def p = DataFactory.createProject()
        int count = 0
        def testResults = []
        while(count < 12) {
            def a = automatedTestService.findOrSave(p, "create a new test with index ${count}")
            def t = new TestResult(automatedTest: a, result: "FAILED")
            testResults.add(t)
            count++
        }
        testRunService.save(new TestRun(name: "name", project: p, testResults: testResults))

        expect:
        testResults.size() == 12

        when:
        def results = testResultService.getMostFailedTestCount(p)

        then:
        results.size() == 10
    }
}
