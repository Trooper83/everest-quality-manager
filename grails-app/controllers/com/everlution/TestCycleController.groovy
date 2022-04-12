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
    def addTests(IterationsCmd cmd) {
        def cycle = testCycleService.get(cmd.testCycleId)
        if(cycle == null) {
            notFound()
            return
        }
        ArrayList<TestCase> tests = []
        if(cmd.testGroups != null) {
            def groups = testGroupService.getAll(cmd.testGroups)
            groups.each { TestGroup tg ->
                tg.testCases.each { TestCase tc ->
                    tests.add(tc)
                }
            }
        } else {
            def testCases = testCaseService.getAll(cmd.testCases)
            testCases.each { TestCase tc ->
                tests.add(tc)
            }
        }
        if (tests.size() > 0) {
            try {
                testCycleService.addTestIterations(cycle, tests)
                flash.message = "Tests successfully added"
            } catch(Exception ignored) {
                flash.error = "Error occurred attempting to add tests"
                redirect uri: "/project/${cycle.releasePlan.project.id}/testCycle/show/${cycle.id}"
                return
            }
        } else { // no tests to add
            flash.message = "No tests added"
        }
        redirect uri: "/project/${cycle.releasePlan.project.id}/testCycle/show/${cycle.id}"
    }

    /**
     * display the create view
     * /testCycle/create
     */
    @Secured("ROLE_BASIC")
    def create(Long releasePlanId) {
        def releasePlan = releasePlanService.get(releasePlanId)
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
        def testCycle = testCycleService.get(id)
        if (testCycle == null) {
            notFound()
            return
        }
        def groups = testCycle.releasePlan.project.testGroups.findAll { TestGroup tg -> tg.testCases.size() > 0 }
        respond testCycle, view: 'show', model: [ testGroups: groups ]
    }

    /**
     * saves a new testCycle
     * @param testCycle
     */
    @Secured("ROLE_BASIC")
    def save(TestCycle testCycle) {
        if (testCycle == null) {
            notFound()
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
                redirect uri: "/project/${testCycle.releasePlan.project.id}/releasePlan/show/${testCycle.releasePlan.id}"
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
}
