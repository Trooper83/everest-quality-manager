package com.everlution.test.ui.functional.specs.project.create

import com.everlution.test.ui.support.data.Credentials

import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    def setup() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to the create page"
        to CreateProjectPage
    }

    void "code field requires three characters"() {
        when: "enter field values"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject("project name", "tt")

        then: "validation message is displayed"
        at CreateProjectPage
    }

    void "name field unique message displayed"() {
        when: "create project with existing name"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject("bootstrap project", "ZED")

        then: "validation message is displayed"
        page.errors.text() ==
                "Property [name] with value [Bootstrap project] must be unique"
    }

    void "code field unique message displayed"() {
        when: "create project with existing code"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject("bootstrap projects", "bsp")

        then: "validation message is displayed"
        page.errors.text() ==
                "Property [code] with value [BSP] must be unique"
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
                "Property [areas] with value [[com.everlution.Area : (unsaved), com.everlution.Area : (unsaved)]] does not pass custom validation"
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
                "Property [environments] with value [[com.everlution.Environment : (unsaved), com.everlution.Environment : (unsaved)]] does not pass custom validation"
    }
}
