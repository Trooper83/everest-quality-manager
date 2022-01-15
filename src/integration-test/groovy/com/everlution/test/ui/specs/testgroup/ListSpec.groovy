package com.everlution.test.ui.specs.testgroup

import com.everlution.TestGroupService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    TestGroupService testGroupService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list page"
        def page = to ListTestGroupPage

        then: "correct headers are displayed"
        page.listTable.getHeaders() == ["Name", "Project"]
    }

    void "home link directs to home view"() {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list bug page"
        def page = to ListTestGroupPage

        when: "click home button"
        page.goToHome()

        then: "at home page"
        at HomePage
    }

    void "new link directs to create view"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list bug page"
        def page = to ListTestGroupPage

        when: "go to create page"
        page.goToCreate()

        then: "at create page"
        at CreateTestGroupPage
    }

    void "create button not displayed on list for Read Only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list page"
        def page = to ListTestGroupPage

        then: "create button is not displayed"
        !page.createLink.displayed
    }

    void "create button displayed on list for users"(String username, String password) {
        given: "login as basic and above user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        when: "go to list page"
        def page = to ListTestGroupPage

        then: "create button is displayed"
        page.createLink.displayed

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list page"
        def listPage = to ListTestGroupPage

        when: "click first in list"
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowTestGroupPage
    }

    void "delete message displays after group deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        def id = testGroupService.list(max: 1).first().id
        go "/testGroup/show/${id}"

        when: "delete"
        def showPage = browser.page(ShowTestGroupPage)
        showPage.delete()

        then: "at list page and message displayed"
        def listPage = at ListTestGroupPage
        listPage.statusMessage.text() ==~ /TestGroup \d+ deleted/
    }
}
