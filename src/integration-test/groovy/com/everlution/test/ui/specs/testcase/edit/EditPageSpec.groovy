package com.everlution.test.ui.specs.testcase.edit


import com.everlution.test.ui.support.data.Usernames

import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToListsPage('Test Cases')

        browser.page(ListTestCasePage).listTable.clickCell("Name", 0)

        browser.page(ShowTestCasePage).goToEdit()
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
