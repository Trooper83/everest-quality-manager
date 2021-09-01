package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    void "home link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to the create test case page"
        to CreateTestCasePage

        when: "click the home button"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to the create test case page"
        to CreateTestCasePage

        when: "click the list link"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.goToList()

        then: "at the list page"
        at ListTestCasePage
    }

    void "required fields indicator displayed for required fields"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to the create test case page"
        to CreateTestCasePage

        expect: "required field indicators displayed"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.areRequiredFieldIndicatorsDisplayed(["executionMethod", "name", "type"])
    }

    void "verify method and type field options"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to the create test case page"
        to CreateTestCasePage

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
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to the create test case page"
        to CreateTestCasePage

        expect: "row count is 1"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.getRowCount() == 1

        when: "add a test step row"
        page.testStepTable.addRow()

        then: "row count is 2"
        page.testStepTable.getRowCount() == 2
    }

    void "remove test step row"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to the create test case page"
        to CreateTestCasePage

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
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to the create test case page"
        to CreateTestCasePage

        expect: "the remove button is not displayed for the first row"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.getRow(0).find("input[value=Remove]").isEmpty()
    }
}
