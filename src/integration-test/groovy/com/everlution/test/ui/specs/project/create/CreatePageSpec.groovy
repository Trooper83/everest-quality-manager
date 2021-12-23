package com.everlution.test.ui.specs.project.create

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    def setup() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to the create page"
        to CreateProjectPage
    }

    void "home link directs to home view"() {
        when: "click the home button"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "click the list link"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.goToList()

        then: "at the list page"
        at ListProjectPage
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.areRequiredFieldIndicatorsDisplayed(["name", "code"])
    }

    void "code field requires three characters"() {
        when: "enter field values"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject("project name", "tt")

        then: "validation message is displayed"
        page.errors.text() ==
                "Property [code] of class [class com.everlution.Project] with value [tt] is less than the minimum size of [3]"
    }

    void "name field unique message displayed"() {
        when: "create project with existing name"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject("bootstrap project", "ZED")

        then: "validation message is displayed"
        page.errors.text() ==
                "Property [name] of class [class com.everlution.Project] with value [bootstrap project] must be unique"
    }

    void "code field unique message displayed"() {
        when: "create project with existing code"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject("bootstrap projects", "bsp")

        then: "validation message is displayed"
        page.errors.text() ==
                "Property [code] of class [class com.everlution.Project] with value [bsp] must be unique"
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        browser.page(CreateProjectPage).getFields() == ["Code *", "Name *", "Areas", "Environments"]
    }

    void "errors message displayed when two areas with same name are added"() {
        given: "add two tags with same name"
        def tag = "Test Area"
        def page = browser.page(CreateProjectPage)
        page.addAreaTag(tag)
        page.addAreaTag(tag)

        when: "attempt to create project"
        page.createProject("Test failure", "DFG")

        then: "message displayed"
        page.errors.text() ==
                "Property [areas] of class [class com.everlution.Project] with value [[com.everlution.Area : (unsaved), com.everlution.Area : (unsaved)]] does not pass custom validation"
    }

    void "errors message displayed when two envs with same name are added"() {
        given: "add two tags with same name"
        def tag = "Test Env"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag(tag)
        page.addEnvironmentTag(tag)

        when: "attempt to create project"
        page.createProject("Test failure", "DFG")

        then: "message displayed"
        page.errors.text() ==
                "Property [environments] of class [class com.everlution.Project] with value [[com.everlution.Environment : (unsaved), com.everlution.Environment : (unsaved)]] does not pass custom validation"
    }
}
