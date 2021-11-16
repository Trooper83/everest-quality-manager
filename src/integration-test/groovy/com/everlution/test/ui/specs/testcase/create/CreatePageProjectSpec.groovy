package com.everlution.test.ui.specs.testcase.create

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageProjectSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        to CreateTestCasePage
    }

    void "project field has no value set"() {
        expect: "default text selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selectedText == "Select a Project..."
        page.projectSelect().selected == ""
    }

    void "failed form submission populates projects"() {
        when: "form submission without project selected"
        def page = browser.page(CreateTestCasePage)
        page.nameInput = "failure"
        page.createButton.click()

        then: "projects are populated"
        page.projectOptions.size() > 1
    }
}
