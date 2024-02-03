package com.everlution.test.service

import com.everlution.AutomatedTestService
import com.everlution.ProjectService
import com.everlution.TestResult
import com.everlution.TestResultService
import com.everlution.TestRunResult
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import spock.lang.Specification

@Integration
@Rollback
class TestResultServiceSpec extends Specification {

    AutomatedTestService automatedTestService
    ProjectService projectService
    TestResultService testResultService

    void "createAndSave returns instance"() {
        when:
        def p = projectService.list(max:1).first()
        def t = new TestRunResult(testName: "test name", result: "Failed")
        def results = testResultService.createAndSave(p, [t])

        then:
        results.size() == 1
    }

    void "createAndSave throws validation exception"() {
        when:
        def p = projectService.list(max:1).first()
        def t = new TestRunResult(testName: "test name", result: "test")
        testResultService.createAndSave(p, [t])

        then:
        thrown(ValidationException)
    }

    void "save persists instance"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "create this or find one")

        when:
        def t = testResultService.save(new TestResult(automatedTest: a, result: "Passed"))

        then:
        t.id != null
    }

    void "findAllByAutomatedTest returns results"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "create this or find one")
        def t = testResultService.save(new TestResult(automatedTest: a, result: "Passed"))

        when:
        def results = testResultService.findAllByAutomatedTest(a)

        then:
        results.contains(t)
    }
}
