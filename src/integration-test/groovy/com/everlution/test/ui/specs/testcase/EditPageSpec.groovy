package com.everlution.test.ui.specs.testcase

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.Step
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    ProjectService projectService
    TestCaseService testCaseService

    void "home link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        and: "go to edit"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.goToEdit()

        when: "click the home button"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        and: "go to edit"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.goToEdit()

        when: "click the list link"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.goToList()

        then: "at the list page"
        at ListTestCasePage
    }

    void "correct fields are displayed"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        def id = testCaseService.list(max: 1).first().id
        go "/testCase/edit/${id}"

        then: "correct fields are displayed"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.getFields() == ["Area", "Description", "Execution Method *", "Name *", "Project *", "Type *"]
    }

    void "required fields indicator displayed for required fields"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        and: "go to edit"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.goToEdit()

        expect: "required field indicators displayed"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.areRequiredFieldIndicatorsDisplayed(["executionMethod", "name", "type"])
    }

    void "verify method and type field options"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        and: "go to edit"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.goToEdit()

        expect: "correct options populate for executionMethod and type"
        EditTestCasePage page = browser.page(EditTestCasePage)
        verifyAll {
            page.executionMethodOptions*.text() == ["Automated", "Manual"]
            page.typeOptions*.text() == ["UI", "API"]
        }

        and: "default values are correct"
        verifyAll( {
            page.executionMethodSelect.value() == "Automated"
            page.typeSelect.value() == "UI"
        })
    }

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
