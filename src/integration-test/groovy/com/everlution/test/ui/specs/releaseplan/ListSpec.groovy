package com.everlution.test.ui.specs.releaseplan

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
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
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

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
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def id = projectService.list(max: 1).first().id
        def page = to(ListReleasePlanPage, id)

        when: "click first in list"
        page.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowReleasePlanPage
    }

    void "create button menu displays"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to show page"
        def id = projectService.list(max: 1).first().id
        def page = to(ListReleasePlanPage, id)

        when:
        page.projectNavButtons.openCreateMenu()

        then:
        page.projectNavButtons.isCreateMenuOpen()
    }

    void "search returns results"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def page = at ProjectHomePage
        page.sideBar.goToProjectDomain('Release Plans')

        when:
        def plansPage = at ListReleasePlanPage
        plansPage.search('')

        then:
        plansPage.listTable.rowCount > 0
    }

    void "search that returns no results displays message"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def page = at ProjectHomePage
        page.sideBar.goToProjectDomain('Release Plans')

        when:
        def plansPage = at ListReleasePlanPage
        plansPage.search('adsfasdf')

        then: "at show page"
        plansPage.statusMessage.text() == "No release plans were found using search term: 'adsfasdf'"
    }

    void "delete message displays after plan deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

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
