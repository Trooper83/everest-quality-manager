package com.everlution

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TestCaseController {

    TestCaseService testCaseService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * Lists all test cases
     * @param max - maximum number of test cases to retrieve
     * @return - list of test cases
     */
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond testCaseService.list(params), model:[testCaseCount: testCaseService.count()]
    }

    def show(Long id) {
        respond testCaseService.get(id)
    }

    def create() {
        respond new TestCase(params)
    }

    def save(TestCase testCase) {
        if (testCase == null) {
            notFound()
            return
        }

        try {
            testCaseService.save(testCase)
        } catch (ValidationException e) {
            respond testCase.errors, view:"create"
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

    def edit(Long id) {
        respond testCaseService.get(id)
    }

    def update(TestCase testCase) {
        if (testCase == null) {
            notFound()
            return
        }

        try {
            testCaseService.save(testCase)
        } catch (ValidationException e) {
            respond testCase.errors, view:"edit"
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
