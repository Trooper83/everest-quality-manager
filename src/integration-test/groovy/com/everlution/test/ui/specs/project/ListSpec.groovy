package com.everlution.test.ui.specs.project

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
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
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        when: "go to list page"
        to ListProjectPage

        then: "correct headers are displayed"
        ListProjectPage page = browser.page(ListProjectPage)
        page.projectTable.getHeaders() == ["Name", "Code"]
    }

    void "home link directs to home view"() {
        given: "login as project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to list page"
        to ListProjectPage

        when: "click home button"
        ListProjectPage page = browser.page(ListProjectPage)
        page.goToHome()

        then: "at home page"
        at HomePage
    }

    void "new project link directs to create view"() {
        given: "login as project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to list page"
        to ListProjectPage

        when: "go to create page"
        ListProjectPage page = browser.page(ListProjectPage)
        page.goToCreateProject()

        then: "at create page"
        at CreateProjectPage
    }

    void "delete message displays after project deleted"() {
        given: "create project"
        def id = projectService.save(new Project(name: "Delete Project Name", code: "DPN")).id

        expect: "id is not null"
        id != null

        when: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to project"
        go "/project/show/${id}"

        and: "delete project"
        ShowProjectPage showPage = browser.page(ShowProjectPage)
        showPage.deleteProject()

        then: "at list page and message displayed"
        ListProjectPage listPage = at ListProjectPage
        listPage.statusMessage.text() ==~ /Project \d+ deleted/
    }
}
