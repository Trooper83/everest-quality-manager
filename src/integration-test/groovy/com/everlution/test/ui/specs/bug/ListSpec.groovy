package com.everlution.test.ui.specs.bug

import com.everlution.BugService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    BugService bugService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list page"
        def page = to ListBugPage

        then: "correct headers are displayed"
        page.bugTable.getHeaders() == ["Name", "Description", "Creator", "Project"]
    }

    void "home link directs to home view"() {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list bug page"
        def page = to ListBugPage

        when: "click home button"
        page.goToHome()

        then: "at home page"
        at HomePage
    }

    void "new bug link directs to create view"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list bug page"
        def page = to ListBugPage

        when: "go to create bug page"
        page.goToCreateBug()

        then: "at create bug page"
        at CreateBugPage
    }

    void "create button not displayed on list for Read Only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list bug page"
        def page = to ListBugPage

        then: "create bug button is not displayed"
        !page.createBugLink.displayed
    }

    void "create button displayed on list for users"(String username, String password) {
        given: "login as basic and above user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        when: "go to list bug page"
        def page = to ListBugPage

        then: "create bug button is displayed"
        page.createBugLink.displayed

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "delete message displays after bug deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show bug"
        def id = bugService.list(max: 1).first().id
        go "/bug/show/${id}"

        when: "delete bug"
        def showPage = browser.page(ShowBugPage)
        showPage.deleteBug()

        then: "at list page and message displayed"
        def listPage = at ListBugPage
        listPage.statusMessage.text() ==~ /Bug \d+ deleted/
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list bug page"
        def listPage = to ListBugPage

        when: "click first bug in list"
        listPage.bugTable.clickCell("Name", 0)

        then: "at show page"
        at ShowBugPage
    }
}
