package com.everlution

import com.everlution.command.TestRunCmd
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class TestRunController {

    TestResultService testResultService
    TestRunService testRunService

    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")//TODO: remove this
    @Transactional
    def save(TestRunCmd testRunCmd) { //TODO: need to secure this endpoint
        if(testRunCmd.hasErrors()) {
            respond [:], status: BAD_REQUEST, formats: ['json']
        }
        else {
            try {
                def results = testResultService.createAndSave(testRunCmd.project, testRunCmd.testResults)
                def testRun = new TestRun(name: testRunCmd.name, project: testRunCmd.project, testResults: results)
                testRunService.save(testRun)
                render text: "TestRun ${testRun.id} created", status: CREATED
            }
            catch(Exception ignored) {
                respond [:], status: BAD_REQUEST, formats: ['json']
            }
        }
    }
}
