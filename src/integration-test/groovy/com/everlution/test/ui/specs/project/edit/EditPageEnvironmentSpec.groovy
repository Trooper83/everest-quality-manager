package com.everlution.test.ui.specs.project.edit

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
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
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")
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

    void "environment tag can be edited"() {
        given: "add a tag"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag("Test Environment Edit")

        when: "edit tag"
        page.editEnvironmentTag("Test Environment Edit", "Edited Environment Tag")

        then: "tag is updated"
        page.isEnvironmentTagDisplayed("Edited Environment Tag")
        !page.isEnvironmentTagDisplayed("Test Environment Edit")
    }

    void "environment tag elements correct after tag edited"() {
        given: "add a tag"
        def tag = "Test Environment Edit"
        def edited = "Edited Environment Tag"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(tag)

        when: "edit tag"
        page.editEnvironmentTag(tag, edited)

        then: "save button removed"
        page.environmentTagSaveButton(edited).size() == 0

        and: "input is hidden"
        !page.isEnvironmentTagHiddenInputDisplayed(edited)

        and: "edit button is displayed"
        page.environmentTagEditButton(edited).size() == 1

        and: "delete button is displayed"
        page.environmentTagRemoveButton(edited).size() == 1
    }

    void "create environment input tooltip text"() {
        when: "add a blank tag name"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag("")

        then: "tooltip is displayed"
        page.tooltip.text() == "Environment Name cannot be blank"
    }

    void "edit environment name input tooltip text"() {
        given: "add tag with name"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag("test")

        when: "edit tag with blank value"
        page.editEnvironmentTag("test", "")

        then: "tooltip displayed"
        page.tooltip.text() == "Environment Name cannot be blank"
    }

    void "environment name cannot be null"() {
        when: "add a blank tag name"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(" ")
        page.editProject()

        then: "message is displayed"
        page.errorMessages.text() ==
                "Property [name] of class [class com.everlution.Environment] cannot be null"
    }
}
