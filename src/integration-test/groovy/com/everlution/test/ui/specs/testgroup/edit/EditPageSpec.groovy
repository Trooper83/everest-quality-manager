package com.everlution.test.ui.specs.testgroup.edit

import com.everlution.TestGroupService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.EditTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageSpec extends GebSpec {

    @Shared int id

    TestGroupService testGroupService

    def setup() {
        id = testGroupService.list(max:1).first().id
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")
        go "/testGroup/edit/${id}"
    }

    void "home link directs to home view"() {
        when: "click the home button"
        def page = browser.page(EditTestGroupPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "click the list link"
        def page = browser.page(EditTestGroupPage)
        page.goToList()

        then: "at the list page"
        at ListTestGroupPage
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        def page = browser.page(EditTestGroupPage)
        page.getFields() == ["Project", "Name *"]
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(EditTestGroupPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }
}
