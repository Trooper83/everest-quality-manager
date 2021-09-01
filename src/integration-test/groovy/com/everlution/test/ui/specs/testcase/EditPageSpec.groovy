package com.everlution.test.ui.specs.testcase

import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestStep
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    TestCaseService testCaseService

    void "home link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

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
        loginPage.login("basic", "password")

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

    void "required fields indicator displayed for required fields"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        and: "go to edit"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.goToEdit()

        expect: "required field indicators displayed"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.areRequiredFieldIndicatorsDisplayed(["executionMethod", "name", "type"])
    }

    void "verify method and type field options"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        and: "go to edit"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.goToEdit()

        expect: "correct options populate for executionMethod and type"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
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
        TestStep testStep = new TestStep(action: "do something", result: "something happened")
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

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
        TestStep testStep = new TestStep(action: "do something", result: "something happened")
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        and: "add a row"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.addRow()

        expect: "row count is 2"
        page.testStepTable.getRowCount() == 2

        when: "remove the second row"
        page.testStepTable.removeRow(1)

        then: "row count is 1"
        page.testStepTable.getRowCount() == 1
    }

    void "first row cannot be removed"() {
        given: "test case"
        TestStep testStep = new TestStep(action: "do something", result: "something happened")
        TestCase testCase = new TestCase(creator: "test", name: "first row cannot be removed", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        expect: "the remove button is not displayed for the first row"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.getRow(0).find("input[value=Remove]").isEmpty()
    }
}
