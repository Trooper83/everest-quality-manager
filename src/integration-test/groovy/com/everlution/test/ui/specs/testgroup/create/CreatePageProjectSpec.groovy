package com.everlution.test.ui.specs.testgroup.create

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageProjectSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        to CreateTestGroupPage
    }

    void "project field has no value set"() {
        expect: "default text selected"
        def page = browser.page(CreateTestGroupPage)
        page.projectSelect().selectedText == "Select a Project..."
        page.projectSelect().selected == ""
    }

    void "failed form submission populates projects"() {
        when: "form submission without project selected"
        def page = browser.page(CreateTestGroupPage)
        page.nameInput = "failure"
        page.createButton.click()

        then: "projects are populated"
        page.projectOptions.size() > 1
    }

    void "null project message displays"() {
        when: "form submission without project selected"
        def page = browser.page(CreateTestGroupPage)
        page.nameInput = "failure"
        page.createButton.click()

        then: "projects are populated"
        page.errorsMessage.text() == "Property [project] of class [class com.everlution.TestGroup] cannot be null"
    }
}
