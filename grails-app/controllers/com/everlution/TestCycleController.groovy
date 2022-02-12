package com.everlution

import com.everlution.command.IterationsCmd
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TestCycleController {

    ReleasePlanService releasePlanService
    TestCaseService testCaseService
    TestCycleService testCycleService
    TestGroupService testGroupService

    static allowedMethods = [save: "POST"]

    /**
     * adds iterations to test cycle
     */
    @Secured("ROLE_BASIC")
    def addIterations(IterationsCmd cmd) {
        def tests = []
        if(cmd.testGroupIds) {
            def groups = testGroupService.getAll(cmd.testGroupIds)
            groups.each {
                tests.add(it.testCases)
            }
        } else {
            def testCases = testCaseService.getAll(cmd.testCaseIds)
            tests.add(testCases)
        }
        def cycle = testCycleService.get(cmd.testCycleId)
        try {
            testCycleService.addTestIterations(cycle, tests)
        } catch(Exception ignored) {
            flash.error = "Error occurred attempting to add tests"
            respond cycle, view: 'show'
            return
        }
        flash.message = "Tests successfully added"
        respond cycle, view: 'show'
    }

    /**
     * display the create view
     * /testCycle/create
     */
    @Secured("ROLE_BASIC")
    def create() {
        def releasePlan = releasePlanService.get(params.releasePlan?.id)
        if(releasePlan == null) {
            notFound()
            return
        }
        respond new TestCycle(params), view: 'create', model: [releasePlan: releasePlan]
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
        def id = params.releasePlan.id as Long
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
    protected void notFound() {
        render status: NOT_FOUND
    }

    /**
     * generic not found response
     * @param id - id of the release plan to redirect to
     */
    protected  void notFound(Long id) {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'testCycle.label', default: 'TestCycle'), params.id])
                redirect controller: "releasePlan", action: "show", id: id, method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
