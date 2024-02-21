package com.everlution

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND

class AutomatedTestController {

    static allowedMethods = [ automatedTests: ["GET", "POST"], show: "GET" ]

    AutomatedTestService automatedTestService
    ProjectService projectService

    @Secured("ROLE_READ_ONLY")
    def automatedTests(Long projectId, Integer max) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        params.max = Math.min(max ?: 25, 100)
        def searchResult
        if(!params.isSearch) { // load view
            searchResult = automatedTestService.findAllByProject(project, params)
        } else {
            searchResult =  automatedTestService.findAllInProjectByFullName(project, params.fullName, params)
        }

        respond searchResult.results, model: [automatedTestCount: searchResult.count, project: project], view: 'automatedTests'
    }

    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond automatedTestService.get(id), view: "show"
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
