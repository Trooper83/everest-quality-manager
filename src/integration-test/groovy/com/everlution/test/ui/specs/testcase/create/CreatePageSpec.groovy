package com.everlution.test.ui.specs.testcase.create

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
        page.areRequiredFieldIndicatorsDisplayed(["name", "project"])
    }

    void "verify method and type field options"() {
        expect: "correct options populate for executionMethod and type"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        verifyAll {
            page.executionMethodOptions*.text() == ["", "Automated", "Manual"]
            page.typeOptions*.text() == ["", "UI", "API"]
        }

        and: "default values are blank"
        verifyAll( {
            page.executionMethodSelect().selected == ""
            page.typeSelect().selected == ""
        })
    }

    void "verify platform options"() {
        expect: "correct options are populated"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }

        and: "default value is blank"
        verifyAll( {
            page.platformSelect().selected == ""
        })
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.getFields() == ["Project *", "Area", "Environments", "Test Groups", "Description", "Execution Method",
                             "Name *", "Platform", "Type"]
    }

    void "null project message displays"() {
        when: "form submission without project selected"
        def page = browser.page(CreateTestCasePage)
        page.nameInput = "failure"
        page.createButton.click()

        then: "projects are populated"
        page.errorsMessage.text() == "Property [project] of class [class com.everlution.TestCase] cannot be null"
    }
}
