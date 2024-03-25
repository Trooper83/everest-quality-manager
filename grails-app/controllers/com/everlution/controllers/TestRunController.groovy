package com.everlution.controllers

import com.everlution.services.project.ProjectService
import com.everlution.services.testrun.TestRunService
import com.everlution.controllers.command.TestRunCmd
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class TestRunController {

    static allowedMethods = [save: "POST", show: "GET", testRuns: ["GET", "POST"]]

    TestRunService testRunService
    ProjectService projectService

    @Secured("ROLE_READ_ONLY")
    def testRuns(Long projectId, Integer max) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        params.max = Math.min(max ?: 25, 100)
        def searchResult
        if(!params.isSearch) { // load view
            searchResult = testRunService.findAllByProject(project, params)
        } else {
            searchResult =  testRunService.findAllInProjectByName(project, params.searchTerm, params)
        }

        respond searchResult.results, model: [testRunCount: searchResult.count, project: project], view: 'testRuns'
    }

    @Secured("ROLE_READ_ONLY")
    def show(Long id, Integer max) {
        params.max = Math.min(max ?: 25, 100)
        def runResults = testRunService.getWithPaginatedResults(id, params)
        respond runResults.testRun, view: "show", model: [results: runResults.results]
    }

    @Secured("ROLE_BASIC")
    @Transactional
    def save(TestRunCmd testRunCmd) {
        if(testRunCmd.hasErrors()) {
            respond [:], status: BAD_REQUEST, formats: ['json']
            return
        }
        try {
            def tr = testRunService.createAndSave(testRunCmd.name, testRunCmd.project, testRunCmd.testResults)
            render text: "TestRun ${tr.id} created", contentType: "application/json", status: CREATED
        }
        catch(Exception ignored) {
            respond [:], status: BAD_REQUEST, formats: ['json']
        }
    }

    /**
     * displays view when notFound (404)
     */
    protected void notFound() {
        request.withFormat {
            '*'{ render status: NOT_FOUND }
        }
    }

    /**
     * displays error view (500)
     */
    protected void error() {
        request.withFormat {
            '*'{ render status: INTERNAL_SERVER_ERROR }
        }
    }
}
