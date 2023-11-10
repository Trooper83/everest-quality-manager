package com.everlution

import com.everlution.command.IterationsCmd
import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*

class TestCycleController {

    TestCaseService testCaseService
    TestCycleService testCycleService
    TestIterationService testIterationService
    TestGroupService testGroupService

    static allowedMethods = [addTests: "POST"]

    /**
     * adds iterations to test cycle
     */
    @Secured("ROLE_BASIC")
    def addTests(IterationsCmd cmd) {
        withForm {
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
        }.invalidToken {
            error()
        }
    }

    /**
     * displays the show view
     * /testCycle/show/${id}
     * @param id - id of the instance to display
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id, Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def cycle = testCycleService.get(id)
        if (cycle == null) {
            notFound()
            return
        }
        def iterations = testIterationService.findAllByTestCycle(cycle, params)
        def groups = cycle.releasePlan.project.testGroups.findAll { TestGroup tg -> tg.testCases.size() > 0 }
        respond cycle, view: 'show', model: [ testGroups: groups, iterations: iterations ]
    }

    /**
     * generic not found response
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
