package com.everlution.test.ui.specs.testgroup.create

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        to CreateTestGroupPage
    }

    void "home link directs to home view"() {
        when: "click the home button"
        def page = browser.page(CreateTestGroupPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "click the list link"
        def page = browser.page(CreateTestGroupPage)
        page.goToList()

        then: "at the list page"
        at ListTestGroupPage
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        def page = browser.page(CreateTestGroupPage)
        page.getFields() == ["Project *", "Name *"]
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(CreateTestGroupPage)
        page.areRequiredFieldIndicatorsDisplayed(["project", "name"])
    }
}
