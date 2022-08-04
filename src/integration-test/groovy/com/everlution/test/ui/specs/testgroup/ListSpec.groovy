package com.everlution.test.ui.specs.testgroup

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Groups')
    }

    void "verify list table headers order"() {
        expect: "correct headers are displayed"
        def page = browser.page(ListTestGroupPage)
        page.listTable.getHeaders() == ["Name", "Project"]
    }

    void "clicking name column directs to show page"() {
        when: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowTestGroupPage
    }

    void "search returns results"() {
        when:
        def page = at ListTestGroupPage
        page.search('')

        then:
        page.listTable.rowCount > 0
    }

    void "search that returns no results displays message"() {
        when:
        def page = at ListTestGroupPage
        page.search('adsfasdf')

        then: "at show page"
        page.statusMessage.text() == "No test groups were found using search term: 'adsfasdf'"
    }

    void "delete message displays after group deleted"() {
        given: "click first test group in list"
        def page = browser.page(ListTestGroupPage)
        page.listTable.clickCell("Name", 0)

        when: "delete"
        def showPage = browser.page(ShowTestGroupPage)
        showPage.delete()

        then: "at list page and message displayed"
        def listPage = at ListTestGroupPage
        listPage.statusMessage.text() ==~ /TestGroup \d+ deleted/
    }

    void "create button not displayed for read only"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        def id = projectService.list().first().id
        go "/project/${id}/testGroups"

        then:
        def page = at ListTestGroupPage
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
        go "/project/${id}/testGroups"

        then:
        def page = at ListTestGroupPage
        page.createButton.displayed

        where:
        email                           | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
    }
}
