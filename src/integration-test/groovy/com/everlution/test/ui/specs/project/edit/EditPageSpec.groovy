package com.everlution.test.ui.specs.project.edit

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
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
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")
        go "/project/edit/${id}"
    }

    void "home link directs to home view"() {
        when: "click the home button"
        EditProjectPage page = at EditProjectPage
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "click the list button"
        EditProjectPage page = at EditProjectPage
        page.goToList()

        then: "at the list page"
        at ListProjectPage
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        EditProjectPage page = at EditProjectPage
        page.areRequiredFieldIndicatorsDisplayed(["name", "code"])
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        browser.page(EditProjectPage).getFields() == ["Code *", "Name *", "Areas", "Environments"]
    }

    void "error message displayed when adding two areas with the same name to project"() {
        when: "add two areas with same name"
        EditProjectPage page = at EditProjectPage
        page.addAreaTag("fail")
        page.addAreaTag("fail")
        page.editProject()

        then: "message displayed"
        page.errorMessages.text() ==
                "Property [areas] of class [class com.everlution.Project] with value [[com.everlution.Area : 1, com.everlution.Area : (unsaved), com.everlution.Area : (unsaved)]] does not pass custom validation"
    }

    void "error message displayed when adding two envs with the same name to project"() {
        when: "add two envs with same name"
        EditProjectPage page = at EditProjectPage
        page.addEnvironmentTag("fail")
        page.addEnvironmentTag("fail")
        page.editProject()

        then: "message displayed"
        page.errorMessages.text() ==
                "Property [environments] of class [class com.everlution.Project] with value [[com.everlution.Environment : 1, com.everlution.Environment : (unsaved), com.everlution.Environment : (unsaved)]] does not pass custom validation"
    }
}
