package com.everlution.test.ui.specs.testcase.edit

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageStepsSpec extends GebSpec {

    ProjectService projectService
    TestCaseService testCaseService

    void "add test step row"() {
        given: "test case"
        Project project = projectService.list(max: 1).first()
        Step testStep = new Step(action: "step1", result: "result1")
        TestCase testCase = new TestCase(creator: "test", name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        expect: "row count is 1"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.testStepTable.getRowCount() == 1

        when: "add a test step row"
        page.testStepTable.addRow()

        then: "row count is 2"
        page.testStepTable.getRowCount() == 2
    }

    void "remove test step row"() {
        given: "test case"
        Project project = projectService.list(max: 1).first()
        Step testStep = new Step(action: "step123", result: "result123")
        TestCase testCase = new TestCase(creator: "test",name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        and: "add a row"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.testStepTable.addRow()

        expect: "row count is 2"
        page.testStepTable.getRowCount() == 2

        when: "remove the second row"
        page.testStepTable.removeRow(1)

        then: "row count is 1"
        page.testStepTable.getRowCount() == 1
    }

    void "removing step adds hidden input"() {
        given: "test case"
        Project project = projectService.list(max: 1).first()
        Step testStep = new Step(action: "step123", result: "result123")
        TestCase testCase = new TestCase(creator: "test",name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        expect:
        testCase.steps.size() == 1

        when: "remove step"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.testStepTable.removeRow(0)

        then: "hidden input added"
        page.stepRemovedInput.size() == 1
    }
}