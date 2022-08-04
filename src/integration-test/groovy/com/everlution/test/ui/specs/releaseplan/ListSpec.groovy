package com.everlution.test.ui.specs.releaseplan

import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
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
        page.listTable.getHeaders() == ["Name", "Status", "Planned Date", "Release Date"]
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

        and:
        def project = projectService.list(max: 1).first()
        def plan = DataFactory.createReleasePlan(project)
        DataFactory.createReleasePlan(project)

        and: "go to show page"

        def page = to(ListReleasePlanPage, project.id)
        page.listTable.clickCell("Name", 0)

        when: "delete"
        def showPage = browser.page(ShowReleasePlanPage)
        showPage.deletePlan()

        then: "at list page and message displayed"
        def listPage = at ListReleasePlanPage
        listPage.statusMessage.text() ==~ /ReleasePlan \d+ deleted/
    }

    void "create button not displayed for read only"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        def id = projectService.list().first().id
        go "/project/${id}/releasePlans"

        then:
        def page = at ListReleasePlanPage
        !page.createButton.displayed

        where:
        email                           | password
        Credentials.READ_ONLY.email     | Credentials.READ_ONLY.password

    }

    void "create button displayed for basic and above"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        def id = projectService.list().first().id
        go "/project/${id}/releasePlans"

        then:
        def page = at ListReleasePlanPage
        page.createButton.displayed

        where:
        email                           | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
    }
}
