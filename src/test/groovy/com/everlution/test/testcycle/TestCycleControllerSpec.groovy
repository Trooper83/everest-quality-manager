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

    void "add tests renders success message"() {
        given:
        request.method = 'POST'
        def group = new TestGroup(name: "group name").save()
        def tc = new TestCase()
        group.addToTestCases(tc)
        IterationsCmd cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testGroups = [group.id]
        def plan = new ReleasePlan()
        plan.id = 1
        def project = new Project()
        project.id = 1
        plan.project = project
        def testCycle = new TestCycle()
        testCycle.id = 1
        testCycle.releasePlan = plan

        and:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getAll(_) >> [group]
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> testCycle
            1 * addTestIterations(_, _)
        }

        when:
        controller.addTests(cmd)

        then:
        controller.flash.message == "Tests successfully added"
        response.redirectedUrl == "/project/1/testCycle/show/1"
    }

    void "add tests with groups that have no tests renders error message"() {
        given:
        request.method = 'POST'
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testGroups = [1]
        def plan = new ReleasePlan()
        plan.id = 1
        def project = new Project()
        project.id = 1
        plan.project = project
        def testCycle = new TestCycle()
        testCycle.id = 1
        testCycle.releasePlan = plan

        when:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getAll(_) >> []
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> testCycle
            0 * addTestIterations(_, _)
        }
        controller.addTests(cmd)

        then:
        controller.flash.message == "No tests added"
        response.redirectedUrl == "/project/1/testCycle/show/1"
    }

    void "add tests renders error message"() {
        given:
        request.method = 'POST'
        def group = new TestGroup(name: "group name").save()
        def tc = new TestCase()
        group.addToTestCases(tc)
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testGroups = [group.id]
        def plan = new ReleasePlan()
        plan.id = 1
        def project = new Project()
        project.id = 1
        plan.project = project
        def testCycle = new TestCycle()
        testCycle.id = 1
        testCycle.releasePlan = plan

        when:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getAll(_) >> [group]
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> testCycle
            1 * addTestIterations(_, _) >> {
                throw new Exception()
            }
        }
        controller.addTests(cmd)

        then:
        controller.flash.error == "Error occurred attempting to add tests"
        response.redirectedUrl == "/project/1/testCycle/show/1"
    }

    void "add tests executes groups logic when group ids specified"() {
        given:
        request.method = 'POST'
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testGroups = [1]
        def plan = new ReleasePlan()
        plan.id = 1
        def project = new Project()
        project.id = 1
        plan.project = project
        def testCycle = new TestCycle()
        testCycle.id = 1
        testCycle.releasePlan = plan

        when:
        controller.addTests(cmd)

        then:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getAll(_) >> []
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> testCycle
            0 * addTestIterations(_, _)
        }
        controller.testCaseService = Mock(TestCaseService) {
            0 * getAll(_)
        }
    }

    void "add tests executes test case logic when test case ids specified"() {
        given:
        request.method = 'POST'
        def cmd = new IterationsCmd()
        cmd.testCycleId = 1
        cmd.testCases = [1]
        def testCycle = new TestCycle()
        def plan = new ReleasePlan()
        plan.id = 1
        def project = new Project()
        project.id = 1
        plan.project = project
        testCycle.id = 1
        testCycle.releasePlan = plan

        when:
        controller.addTests(cmd)

        then:
        controller.testGroupService = Mock(TestGroupService) {
            0 * getAll(_) >> []
        }
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(_) >> testCycle
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
        request.method = 'POST'
        controller.addTests(new IterationsCmd())

        then:
        status == 404
    }

    void "add tests action method type"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.addTests(new IterationsCmd())

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'PUT'        | _
        'PATCH'      | _
    }
}






