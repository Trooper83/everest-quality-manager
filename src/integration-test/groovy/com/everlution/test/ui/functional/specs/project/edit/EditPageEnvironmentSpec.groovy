package com.everlution.test.ui.functional.specs.project.edit

import com.everlution.services.project.ProjectService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageEnvironmentSpec extends GebSpec {

    ProjectService projectService
    @Shared int id

    def setup() {
        id = projectService.list(max: 1)[0].id
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)
        go "/project/edit/${id}"
    }

    void "environment tag is added and removed"() {
        given: "add a tag"
        def tag = "Test Environment"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(tag)

        expect: "tag is displayed"
        page.isEnvironmentTagDisplayed(tag)

        when: "remove tag"
        page.removeEnvironmentTag(tag)

        then: "tag is not displayed"
        !page.isEnvironmentTagDisplayed(tag)
    }

    void "environment tag input is cleared when tag added"() {
        given: "add a tag"
        def tag = "Test Environment"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(tag)

        expect: "tag is displayed and input is cleared"
        page.isEnvironmentTagDisplayed(tag)
        page.environmentInput.value() == ""
    }

    void "hidden environment input is added"() {
        given: "add a tag"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag("Test Environment")

        expect: "hidden input is present and not displayed"
        !page.isEnvironmentTagHiddenInputDisplayed("Test Environment")
    }

    void "removing one tag does not remove all"() {
        given: "add two tags"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag("Test Environment1")
        page.addEnvironmentTag("Test Environment2")

        expect: "tag is found"
        page.isEnvironmentTagDisplayed("Test Environment2")

        when: "remove one tag"
        page.removeEnvironmentTag("Test Environment1")

        then: "only the selected tag is removed"
        page.isEnvironmentTagDisplayed("Test Environment2")
    }

    void "create environment input tooltip text"() {
        when: "add a blank tag name"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag("")

        then: "tooltip is displayed"
        page.getToolTipText() == "Environment Name cannot be blank"
    }
}
