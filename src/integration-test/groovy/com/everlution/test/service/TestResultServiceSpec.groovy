package com.everlution.test.service

import com.everlution.ProjectService
import com.everlution.TestResultService
import com.everlution.TestRunResult
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import spock.lang.Specification

@Integration
@Rollback
class TestResultServiceSpec extends Specification {

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
}
