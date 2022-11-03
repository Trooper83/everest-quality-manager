package com.everlution.test.ui.functional.specs.project

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials

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
        listPage.search('bootstrap')

        then: "at show page"
        listPage.projectTable.rowCount > 0
        listPage.nameInput.text == 'bootstrap'
    }
}
