package com.everlution

import com.everlution.command.IterationsCmd
import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*

class TestCycleController {

    TestCaseService testCaseService
    TestCycleService testCycleService
    TestGroupService testGroupService

    static allowedMethods = [addTests: "POST"]

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
     * generic not found response
     */
    protected void notFound() {
        render status: NOT_FOUND
    }
}
