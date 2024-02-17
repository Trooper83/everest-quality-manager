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
}
