package com.everlution.test.ui.specs.releaseplan

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    ProjectService projectService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list page"
        def id = projectService.list(max: 1).first().id
        def page = to(ListReleasePlanPage, id)

        then: "correct headers are displayed"
        page.listTable.getHeaders() == ["Name", "Project"]
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list page"
        def id = projectService.list(max: 1).first().id
        def page = to(ListReleasePlanPage, id)

        when: "click first in list"
        page.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowReleasePlanPage
    }

    void "delete message displays after plan deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        def id = projectService.list(max: 1).first().id
        def page = to(ListReleasePlanPage, id)
        page.listTable.clickCell("Name", 0)

        when: "delete"
        def showPage = browser.page(ShowReleasePlanPage)
        showPage.deletePlan()

        then: "at list page and message displayed"
        def listPage = at ListReleasePlanPage
        listPage.statusMessage.text() ==~ /ReleasePlan \d+ deleted/
    }
}
