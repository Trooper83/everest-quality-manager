package com.everlution.test.testcycle

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestCycle
import com.everlution.TestCycleController
import com.everlution.TestCycleService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.command.IterationsCmd
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class TestCycleControllerSpec extends Specification implements ControllerUnitTest<TestCycleController>, DomainUnitTest<TestCycle> {

    def populateValidParams(params) {
        assert params != null
        def plan = new ReleasePlan().save()

        params["name"] = 'someValidName'
        params["releasePlan.id"] = plan.id
        params["releasePlan"] = plan
    }

    void "test the create action returns the correct view"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(_) >> new ReleasePlan()
        }

        when:"the create action is executed"
        populateValidParams(params)
        controller.create()

        then:"the model is correctly created"
        view == "create"
    }

    void "test the create action returns the correct model"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(_) >> new ReleasePlan()
        }

        when:"the create action is executed"
        populateValidParams(params)
        controller.create()

        then:"the model is correctly created"
        def m = model
        model.testCycle!= null
        model.releasePlan != null
    }

    void "create action renders 404 not found view with null release plan"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(_) >> null
        }

        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        status == 404
    }

    void "null pointer not thrown when release plan not found in params"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(_) >> null
        }

        when:
        controller.create()

        then:
        notThrown(NullPointerException)
    }

    void "save action with a null instance"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        controller.save(null)

        then:"404 error is returned"
        response.redirectedUrl == '/releasePlan/show/1'
        flash.message != null
    }

    void "save action correctly persists"() {
        given:
        controller.testCycleService = Mock(TestCycleService) {
            1 * save(_ as TestCycle)
        }

        when:"save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def testCycle = new TestCycle(params)
        testCycle.id = 1

        controller.save(testCycle)

        then:"redirect is issued to the show action"
        response.redirectedUrl == '/releasePlan/show/1'
        controller.flash.message != null
    }

    void "save action with an invalid instance"() {
        given:
        controller.testCycleService = Mock(TestCycleService) {
            1 * save(_ as TestCycle) >> { TestCycle testCycle ->
                throw new ValidationException("Invalid instance", testCycle.errors)
            }
        }

        when:"save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def testCycle = new TestCycle()
        controller.save(testCycle)

        then:"create view is rendered again with the correct model"
        model.testCycle != null
        view == 'create'
    }

    void "save action method type"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def cycle = new TestCycle(params)

        when:
        controller.save(cycle)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'PUT'        | _
    }

    void "show action with a null id"() {
        given:
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(null) >> null
        }

        when:"show action is executed with a null domain"
        controller.show(null)

        then:"404 error is returned"
        response.status == 404
    }

    void "show action with a valid id"() {
        given:
        def plan = new ReleasePlan(name: "name", project: new Project())
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(2) >> new TestCycle(releasePlan: plan)
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.testCycle instanceof TestCycle
        model.testGroups != null
        view == 'show'
    }

    void "show action model populates only test groups with test cases"() {
        given:
        def group1 = new TestGroup(name: "group 1").save()
        def group2 = new TestGroup(name: "group 2", testCases: []).save()
        def person = new Person(email: "test@test.com", password: "password").save()
        def project = new Project(name: "name of project testing", code: "999").save()
        project.addToTestGroups(group1).addToTestGroups(group2)
        def tc = new TestCase(name: "testcase", person: person, project: project).save()
        group1.addToTestCases(tc)
        def plan = new ReleasePlan(name: "release", project: project).save()
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(1) >> new TestCycle(releasePlan: plan)
        }

        when:
        controller.show(1)

        then:
        model.testGroups == [group1]
    }

    void "add iterations renders success message"() {
        given:
        def group = new TestGroup(name: "group name").save()
        def tc = new TestCase()
        group.addToTestCases(tc)
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testGroups = [group.id]

        when:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getAll(_) >> [group]
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> new TestCycle()
            1 * addTestIterations(_, _)
        }
        controller.addTests(cmd)

        then:
        controller.flash.message == "Tests successfully added"
        response.redirectedUrl == "/testCycle/show"
    }

    void "add iterations with groups that have no tests renders error message"() {
        given:
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testGroups = [1]

        when:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getAll(_) >> []
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> new TestCycle()
            0 * addTestIterations(_, _)
        }
        controller.addTests(cmd)

        then:
        controller.flash.message == "No tests added"
        response.redirectedUrl == "/testCycle/show"
    }

    void "add iterations renders error message"() {
        given:
        def group = new TestGroup(name: "group name").save()
        def tc = new TestCase()
        group.addToTestCases(tc)
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testGroups = [group.id]

        when:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getAll(_) >> [group]
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> new TestCycle()
            1 * addTestIterations(_, _) >> {
                throw new Exception()
            }
        }
        controller.addTests(cmd)

        then:
        controller.flash.error == "Error occurred attempting to add tests"
        response.redirectedUrl == "/testCycle/show"
    }

    void "add iterations executes groups logic when group ids specified"() {
        given:
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testGroups = [1]

        when:
        controller.addTests(cmd)

        then:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getAll(_) >> []
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> new TestCycle()
            0 * addTestIterations(_, _)
        }
        controller.testCaseService = Mock(TestCaseService) {
            0 * getAll(_)
        }
    }

    void "add iterations executes test case logic when test case ids specified"() {
        given:
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testCases = [1]

        when:
        controller.addTests(cmd)

        then:
        controller.testGroupService = Mock(TestGroupService) {
            0 * getAll(_) >> []
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> new TestCycle()
            0 * addTestIterations(_, _)
        }
        controller.testCaseService = Mock(TestCaseService) {
            1 * getAll(_)
        }
    }

    void "add tests returns not found"() {
        given:
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> null
        }

        when:
        controller.addTests(new IterationsCmd())

        then:
        status == 404
    }
}






