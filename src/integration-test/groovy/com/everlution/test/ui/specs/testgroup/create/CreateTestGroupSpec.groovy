package com.everlution.test.ui.specs.testgroup.create

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateTestGroupSpec extends GebSpec {

    void "authorized users can create test group"(String username, String password) {
        given: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        def page = to CreateTestGroupPage

        when: "create"
        page.createTestGroup()

        then: "at show page"
        at ShowTestGroupPage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "all create form data persists"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to the create page & create instance"
        def page = to CreateTestGroupPage
        def name = DataFactory.testGroup().name
        page.createTestGroup(name, "1")

        then: "at show page"
        def show = at ShowTestGroupPage
        verifyAll {
            show.nameValue.text() == name
            show.projectValue.text() == "bootstrap project"
        }
    }
}
