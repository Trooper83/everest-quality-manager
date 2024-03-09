package com.everlution.test.ui.functional.specs.project

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    ProjectService projectService

    void "verify list table headers order"() {
        given: "login as project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        when: "go to list page"
        to ListProjectPage

        then: "correct headers are displayed"
        ListProjectPage page = browser.page(ListProjectPage)
        page.projectTable.getHeaders() == ["Name", "Code"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def page = to ListProjectPage

        and:
        page.projectTable.sortColumn(column)

        expect: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=asc')

        when:
        page.projectTable.sortColumn(column)

        then: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=desc')

        where:
        column | propName
        'Name' | 'name'
        'Code' | 'code'
    }

    void "delete message displays after project deleted"() {
        given: "create project"
        def id = projectService.save(new Project(name: "Delete Project Name", code: "DPN")).id

        expect: "id is not null"
        id != null

        when: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to project"
        go "/project/show/${id}"

        and: "delete project"
        def showPage = browser.page(ShowProjectPage)
        showPage.deleteProject()

        then: "at list page and message displayed"
        ListProjectPage listPage = at ListProjectPage
        listPage.statusMessage.text() ==~ /Project \S+ deleted/
    }

    void "clicking name column directs to show page"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to list project page"
        def listPage = to ListProjectPage

        when: "click first in list"
        listPage.projectTable.clickCell("Name", 0)

        then: "at show page"
        at ProjectHomePage
    }

    void "search returns results"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to list project page"
        def listPage = to ListProjectPage

        when:
        listPage.searchModule.search('bootstrap')

        then: "at show page"
        listPage.projectTable.rowCount > 0
        listPage.searchModule.searchInput.text == 'bootstrap'
    }

    void "reset button reloads results"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and:
        def page = to(ListProjectPage)
        page.searchModule.search('project')

        expect:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'project'

        when:
        page.searchModule.resetSearch()

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == ''
    }
}
