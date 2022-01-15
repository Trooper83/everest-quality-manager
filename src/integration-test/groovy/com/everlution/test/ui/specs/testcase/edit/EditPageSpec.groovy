package com.everlution.test.ui.specs.testcase.edit

import com.everlution.TestCaseService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    TestCaseService testCaseService

    def setup() {
        def id = testCaseService.list(max: 1).first().id
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")
        go "/testCase/edit/${id}"
    }

    void "home link directs to home view"() {
        when: "click the home button"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "click the list link"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.goToList()

        then: "at the list page"
        at ListTestCasePage
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.getFields() == ["Project", "Area", "Environments", "Test Groups", "Description", "Execution Method", "Name *",
                             "Platform", "Type"]
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }

    void "verify method and type field options"() {
        expect: "correct options populate for executionMethod and type"
        EditTestCasePage page = browser.page(EditTestCasePage)
        verifyAll {
            page.executionMethodOptions*.text() == ["", "Automated", "Manual"]
            page.typeOptions*.text() == ["", "UI", "API"]
        }
    }

    void "verify platform field options"() {
        expect: "correct options populate for executionMethod and type"
        EditTestCasePage page = browser.page(EditTestCasePage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }
    }
}
