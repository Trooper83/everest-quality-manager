package com.everlution.test.ui.specs.project

import com.everlution.ProjectService
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageSpec extends GebSpec {

    ProjectService projectService
    @Shared int id

    def setup() {
        id = projectService.list(max: 1)[0].id
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("project_admin", "password")
    }

    void "home link directs to home view"() {
        given: "go to edit project page"
        go "/project/edit/${id}"

        when: "click the home button"
        EditProjectPage page = at EditProjectPage
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        given: "go to edit project page"
        go "/project/edit/${id}"

        when: "click the list button"
        EditProjectPage page = at EditProjectPage
        page.goToList()

        then: "at the list page"
        at ListProjectPage
    }

    void "required fields indicator displayed for required fields"() {
        given: "go to edit project page"
        go "/project/edit/${id}"

        expect: "required field indicators displayed"
        EditProjectPage page = at EditProjectPage
        page.areRequiredFieldIndicatorsDisplayed(["name", "code"])
    }
}
