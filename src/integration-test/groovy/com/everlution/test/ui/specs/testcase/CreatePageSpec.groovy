package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        to CreateTestCasePage
    }

    void "home link directs to home view"() {
        when: "click the home button"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "click the list link"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.goToList()

        then: "at the list page"
        at ListTestCasePage
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.areRequiredFieldIndicatorsDisplayed(["executionMethod", "name", "type", "project"])
    }

    void "verify method and type field options"() {
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
        expect: "row count is 1"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.getRowCount() == 1

        when: "add a test step row"
        page.testStepTable.addRow()

        then: "row count is 2"
        page.testStepTable.getRowCount() == 2
    }

    void "remove test step row"() {
        setup: "add a row"
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
        expect: "the remove button is not displayed for the first row"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.getRow(0).find("input[value=Remove]").isEmpty()
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.getFields() == ["Description", "Execution Method *", "Name *", "Project *", "Type *"]
    }
}
