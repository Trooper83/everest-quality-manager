package com.everlution

import com.everlution.command.TestRunCmd
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class TestRunController {

    static allowedMethods = [save: "POST"]

    TestResultService testResultService
    TestRunService testRunService

    @Secured("ROLE_BASIC")
    @Transactional
    def save(TestRunCmd testRunCmd) {
        if(testRunCmd.hasErrors()) {
            respond [:], status: BAD_REQUEST, formats: ['json']
            return
        }
        try {
            def results = testResultService.createAndSave(testRunCmd.project, testRunCmd.testResults)
            def t = new TestRun(name: testRunCmd.name, project: testRunCmd.project, testResults: results)
            def tr = testRunService.save(t)
            render text: "TestRun ${tr.id} created", contentType: "application/json", status: CREATED
        }
        catch(Exception ignored) {
            respond [:], status: BAD_REQUEST, formats: ['json']
        }
    }
}
