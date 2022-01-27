package com.everlution

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TestCycleController {

    TestCycleService testCycleService

    static allowedMethods = [save: "POST"]

    /**
     * display the create view
     * /testCycle/create
     */
    @Secured("ROLE_BASIC")
    def create() {
        respond new TestCycle(params), view: 'create'
    }

    /**
     * displays the show view
     * /testCycle/show/${id}
     * @param id - id of the instance to display
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond testCycleService.get(id), view: 'show'
    }

    /**
     * saves a new testCycle
     * @param testCycle
     */
    @Secured("ROLE_BASIC")
    def save(TestCycle testCycle) {
        def id = null //TODO: need to get releaseplan id from url?
        if (testCycle == null) {
            notFound(id)
            return
        }

        try {
            testCycleService.save(testCycle)
        } catch (ValidationException ignored) {
            respond testCycle.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'testCycle.label', default: 'TestCycle'), testCycle.id])
                redirect controller: "releasePlan", action: "show", id: id, method: "GET"
            }
            '*' { respond testCycle, [status: CREATED] }
        }
    }

    /**
     * generic not found response
     */
    protected void notFound(Long id) {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'testCycle.label', default: 'TestCycle'), params.id])
                redirect controller: "releasePlan", action: "show", id: id, method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
