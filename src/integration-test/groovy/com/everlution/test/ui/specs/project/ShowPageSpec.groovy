package com.everlution.test.ui.specs.project

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class ShowPageSpec extends GebSpec {

    @Shared int id

    ProjectService projectService

    def setup() {
        id = projectService.list(max: 1)[0].id
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")
    }

    void "status message displayed after project created"() {
        when: "go to the create page"
        to CreateProjectPage

        and: "create a project"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject("status message project", "SMP")

        then: "at show page with message displayed"
        ShowProjectPage showPage = at ShowProjectPage
        showPage.statusMessage.text() ==~ /Project \d+ created/
    }

    void "home link directs to home view"() {
        when: "go to show project page"
        go "/project/show/${id}"

        and: "click the home button"
        ShowProjectPage page = browser.page(ShowProjectPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "go to show project page"
        go "/project/show/${id}"

        and: "click the list button"
        ShowProjectPage page = browser.page(ShowProjectPage)
        page.goToList()

        then: "at the list page"
        at ListProjectPage
    }

    void "create link directs to create view"() {
        when: "go to show project page"
        go "/project/show/${id}"

        and: "click the create button"
        ShowProjectPage page = browser.page(ShowProjectPage)
        page.goToCreate()

        then: "at the create page"
        at CreateProjectPage
    }

    void "edit link directs to edit view"() {
        when: "go to show project page"
        go "/project/show/${id}"

        and: "click the edit button"
        ShowProjectPage page = browser.page(ShowProjectPage)
        page.goToEdit()

        then: "at the edit page"
        at EditProjectPage
    }

    void "correct fields are displayed"() {
        when: "go to show project page"
        go "/project/show/${id}"

        then: "correct fields are displayed"
        ShowProjectPage page = browser.page(ShowProjectPage)
        page.getFields() == ["Name", "Code"]
    }

    void "project not deleted if alert is canceled"() {
        when: "go to show project page"
        go "/project/show/${id}"

        and: "click delete and cancel | verify message"
        ShowProjectPage showPage = browser.page(ShowProjectPage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show page"
        at ShowProjectPage
    }

    void "updated message displays after updating project"() {
        when: "go to edit project page"
        go "/project/edit/${id}"

        and: "edit a project"
        EditProjectPage page = at EditProjectPage
        page.editProject()

        then: "at show page with message displayed"
        ShowProjectPage showPage = at ShowProjectPage
        showPage.statusMessage.text() == "Project ${id} updated"
    }
}
