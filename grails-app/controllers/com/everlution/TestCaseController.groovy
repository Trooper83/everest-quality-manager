package com.everlution

import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import org.springframework.dao.DataIntegrityViolationException

import static org.springframework.http.HttpStatus.*

class TestCaseController {

    ProjectService projectService
    SpringSecurityService springSecurityService
    TestCaseService testCaseService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all test cases
     * /testCase
     * @param projectId - number of the project
     * @return - list of test cases
     */
    @Secured("ROLE_READ_ONLY")
    def testCases(Long projectId, Integer max) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        params.max = Math.min(max ?: 25, 100)
        def searchResults
        if(!params.isSearch) { // load view
            searchResults = testCaseService.findAllByProject(project, params)

        } else { // perform search
            searchResults = testCaseService.findAllInProjectByName(project, params.searchTerm, params)

        }
        respond searchResults.results, model: [testCaseCount: searchResults.count, project: project], view: 'testCases'
    }

    /**
     * shows a single test case
     * /testCase/show/$id
     * @param id - id of the case to display
     * @return - a single test case
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond testCaseService.get(id), view:"show"
    }

    /**
     * display the create test case view
     * /testCase/create
     * @return - create test case view
     */
    @Secured("ROLE_BASIC")
    def create(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }
        respond new TestCase(params), model: [project: project], view: 'create'
    }

    /**
     * saves a test case
     * @param testCase - the test case to save
     */
    @Secured("ROLE_BASIC")
    def save(TestCase testCase) {
        withForm {
            if (testCase == null) {
                notFound()
                return
            }

            Person person = springSecurityService.getCurrentUser() as Person
            testCase.person = person
            try {
                testCaseService.save(testCase)
            } catch (ValidationException ignored) {
                def project = projectService.read(testCase.project.id)
                params.projectId = project.id
                respond testCase.errors, view:"create", model: [ project: project ]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: "default.created.message", args: [message(code: "testCase.name", default: "TestCase"), testCase.id])
                    redirect uri: "/project/${testCase.project.id}/testCase/show/${testCase.id}"
                }
                "*" { respond testCase, [status: CREATED] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * displays the edit test case view
     * /testCase/edit
     * @param id - id of the test to edit
     */
    @Secured("ROLE_BASIC")
    def edit(Long id) {
        respond testCaseService.get(id), view:"edit"
    }

    /**
     * updates a test case
     * @param testCase - test case values to update
     */
    @Secured("ROLE_BASIC")
    def update(TestCase testCase, RemovedItems removedItems, Long projectId) {
        withForm {
            if (testCase == null || projectId == null) {
                notFound()
                return
            }

            def testProjectId = testCase.project.id
            if (projectId != testProjectId) {
                notFound()
                return
            }

            try {
                testCaseService.saveUpdate(testCase, removedItems)
            } catch (ValidationException e) {
                def tc = testCaseService.read(testCase.id)
                tc.errors = e.errors
                render view:'edit', model: [testCase: tc]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: "default.updated.message", args: [message(code: "testCase.label", default: "TestCase"), testCase.id])
                    redirect uri: "/project/${testCase.project.id}/testCase/show/${testCase.id}"
                }
                "*"{ respond testCase, [status: OK] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * deletes a test case
     * @param id
     * @return
     */
    @Secured("ROLE_BASIC")
    def delete(Long id, Long projectId) {
        withForm {
            if (id == null || projectId == null) {
                notFound()
                return
            }
            def testCase = testCaseService.read(id)
            if (testCase.project.id != projectId) {
                notFound()
                return
            }

            try {
                testCaseService.delete(id)
            } catch(DataIntegrityViolationException ignored) {
                flash.error = "Test Case has associated Test Iterations and cannot be deleted"
                render view: 'show', model: [ testCase: testCaseService.get(id) ]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: "default.deleted.message", args: [message(code: "testCase.label", default: "TestCase"), id])
                    redirect uri: "/project/${projectId}/testCases"
                }
                "*"{ render status: NO_CONTENT }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * displays view when notFound (404)
     */
    protected void notFound() {
        request.withFormat {
            "*"{ render status: NOT_FOUND }
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
