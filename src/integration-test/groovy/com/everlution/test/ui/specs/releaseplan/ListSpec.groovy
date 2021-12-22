package com.everlution.test.ui.specs.releaseplan

import com.everlution.ReleasePlanService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    ReleasePlanService releasePlanService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list page"
        def page = to ListReleasePlanPage

        then: "correct headers are displayed"
        page.plansTable.getHeaders() == ["Name", "Project", "Test Cycles"]
    }

    void "home link directs to home view"() {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list bug page"
        def page = to ListReleasePlanPage

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
        def page = to ListReleasePlanPage

        when: "go to create page"
        page.goToCreatePlan()

        then: "at create page"
        at CreateReleasePlanPage
    }

    void "create button not displayed on list for Read Only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list page"
        def page = to ListReleasePlanPage

        then: "create button is not displayed"
        !page.createLink.displayed
    }

    void "create button displayed on list for users"(String username, String password) {
        given: "login as basic and above user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        when: "go to list page"
        def page = to ListReleasePlanPage

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
        def listPage = to ListReleasePlanPage

        when: "click first in list"
        listPage.plansTable.clickCell("Name", 0)

        then: "at show page"
        at ShowReleasePlanPage
    }

    void "delete message displays after plan deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        def id = releasePlanService.list(max: 1).first().id
        go "/releasePlan/show/${id}"

        when: "delete"
        def showPage = browser.page(ShowReleasePlanPage)
        showPage.deletePlan()

        then: "at list page and message displayed"
        def listPage = at ListReleasePlanPage
        listPage.statusMessage.text() ==~ /ReleasePlan \d+ deleted/
    }
}
