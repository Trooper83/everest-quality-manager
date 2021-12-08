package com.everlution

import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TestCaseController {

    ProjectService projectService
    TestCaseService testCaseService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all test cases
     * /testCase/index
     * @param max - maximum number of test cases to retrieve
     * @return - list of test cases
     */
    @Secured("ROLE_READ_ONLY")
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond testCaseService.list(params), model:[testCaseCount: testCaseService.count()]
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
    def create() {
        respond new TestCase(params), model: [projects: projectService.list()]
    }

    /**
     * saves a test case
     * @param testCase - the test case to save
     */
    @Secured("ROLE_BASIC")
    def save(TestCase testCase) {
        if (testCase == null) {
            notFound()
            return
        }

        try {
            testCaseService.save(testCase)
        } catch (ValidationException e) {
            respond testCase.errors, view:"create", model: [projects: projectService.list()]
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: "default.created.message", args: [message(code: "testCase.name", default: "TestCase"), testCase.id])
                redirect testCase
            }
            "*" { respond testCase, [status: CREATED] }
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
    def update(TestCase testCase, RemovedItems removedItems) {
        if (testCase == null) {
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
                redirect testCase
            }
            "*"{ respond testCase, [status: OK] }
        }
    }

    /**
     * deletes a test case
     * @param id
     * @return
     */
    @Secured("ROLE_BASIC")
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        testCaseService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: "default.deleted.message", args: [message(code: "testCase.label", default: "TestCase"), id])
                redirect action:"index", method:"GET"
            }
            "*"{ render status: NO_CONTENT }
        }
    }

    /**
     * displays view when notFound (404)
     */
    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: "default.not.found.message", args: [message(code: "testCase.name", default: "TestCase"), params.id])
                redirect action: "index", method: "GET"
            }
            "*"{ render status: NOT_FOUND }
        }
    }
}
