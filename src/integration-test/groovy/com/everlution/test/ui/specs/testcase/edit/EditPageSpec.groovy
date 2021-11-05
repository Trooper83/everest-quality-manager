package com.everlution.test.ui.specs.testcase.edit

import com.everlution.TestCaseService
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
        page.getFields() == ["Project", "Area", "Description", "Execution Method *", "Name *", "Type *"]
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
}
