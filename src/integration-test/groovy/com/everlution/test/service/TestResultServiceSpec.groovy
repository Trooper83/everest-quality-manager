package com.everlution.test.service

import com.everlution.AutomatedTestService
import com.everlution.ProjectService
import com.everlution.TestResult
import com.everlution.TestResultService
import com.everlution.TestRun
import com.everlution.TestRunService
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
        def t = new TestResult(automatedTest: a, result: "Passed")
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
        def t = new TestResult(automatedTest: a, result: "Passed")
        def t1 = new TestResult(automatedTest: a, result: "Failed")
        def t2 = new TestResult(automatedTest: a, result: "Skipped")
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
}
