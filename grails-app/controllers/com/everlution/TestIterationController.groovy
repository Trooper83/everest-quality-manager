package com.everlution

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class TestIterationController {

    TestIterationService testIterationService

    static allowedMethods = [update: "PUT"]

    /**
     * displays the execute view
     * /testIteration/execute/${id}
     * @param id - id of the instance to display
     */
    @Secured("ROLE_BASIC")
    def execute(Long id) {
        respond testIterationService.get(id), view: 'execute'
    }

    /**
     * displays the show view
     * /testIteration/show/${id}
     * @param id - id of the instance to display
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond testIterationService.get(id), view: 'show'
    }

    /**
     * updates a test iteration data
     * @param testIteration - iteration to update
     */
    @Secured("ROLE_BASIC")
    def update(TestIteration testIteration, Long projectId) {
        withForm {
            if (testIteration == null || projectId == null) {
                notFound()
                return
            }

            def testProjectId = testIteration.testCycle.releasePlan.project.id
            if (projectId != testProjectId) {
                notFound()
                return
            }

            try {
                testIterationService.save(testIteration)
            } catch (ValidationException ignored) {
                def t = testIterationService.read(testIteration.id)
                t.errors = testIteration.errors
                render view: 'execute', model: [testIteration: t]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'testIteration.label', default: 'Test Iteration'), testIteration.id])
                    redirect uri: "/project/${testIteration.testCycle.releasePlan.project.id}/testIteration/show/${testIteration.id}"
                }
                '*'{ respond testIteration, [status: OK] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * displays view when notFound (404)
     */
    protected void notFound() {
        render status: NOT_FOUND
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
