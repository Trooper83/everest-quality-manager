package com.everlution.test.ui.functional.specs.project.edit

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
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

        and:
        page.environmentTagEditButton(edited).size() == 0

        and:
        page.environmentTagRemoveButton(edited).size() == 0
    }

    void "create environment input tooltip text"() {
        when: "add a blank tag name"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag("")

        then: "tooltip is displayed"
        page.getToolTipText() == "Environment Name cannot be blank"
    }

    void "edit environment name input tooltip displayed when empty"(String text) {
        given: "add tag with name"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag("test")

        when: "edit tag with blank value"
        page.editEnvironmentTag("test", text)

        then: "tooltip displayed"
        page.getToolTipText() == "Environment Name cannot be blank"

        where:
        text << ['', ' ']
    }

    void "environment tag edit fields removed or hidden when cancelled"() {
        given: "add a tag"
        def tag = "Test Env Edit"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(tag)

        and: "edit tag"
        page.displayEnvironmentTagEditFields(tag)

        when: "cancel edit"
        page.cancelEnvironmentTagEdit(tag)

        then: "save button removed"
        page.environmentTagSaveButton(tag).size() == 0

        and: "input is hidden"
        !page.isEnvironmentTagHiddenInputDisplayed(tag)

        and:
        page.environmentTagEditButton(tag).size() == 0

        and:
        page.environmentTagEditButton(tag).size() == 0
    }

    void "environment tag edit fields removed or hidden when click outside of input field"() {
        given: "add a tag"
        def tag = "Test Env Edit"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(tag)

        and: "edit tag"
        page.displayEnvironmentTagEditFields(tag)

        when: "click outside input"
        page.environmentInput.click()

        then: "save button removed"
        page.environmentTagSaveButton(tag).size() == 0

        and: "input is hidden"
        !page.isEnvironmentTagHiddenInputDisplayed(tag)

        and:
        page.environmentTagEditButton(tag).size() == 0

        and:
        page.environmentTagRemoveButton(tag).size() == 0
    }

    void "environment tag edit fields removed or hidden when second tag is edited"() {
        given: "add a tag"
        def tag = "Test Env Edit"
        def tag1 = "Second Test Env Edit"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(tag)
        page.addEnvironmentTag(tag1)

        when: "edit tag"
        page.displayEnvironmentTagEditFields(tag)
        page.displayEnvironmentTagEditFields(tag1)

        then: "save button removed"
        page.environmentTagSaveButton(tag).size() == 0

        and: "input is hidden"
        !page.isEnvironmentTagHiddenInputDisplayed(tag)

        and:
        page.environmentTagEditButton(tag).size() == 0

        and:
        page.environmentTagRemoveButton(tag).size() == 0
    }

    void "environment tag edits not persisted when cancelled"() {
        given: "add a tag"
        def tag = "Test Env Edit"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(tag)

        and: "edit tag"
        page.displayEnvironmentTagEditFields(tag)

        and: "edit tag value"
        page.environmentTagInput(tag).value("should not persist")

        when: "cancel edit"
        page.cancelEnvironmentTagEdit(tag)

        then: "save button removed"
        page.displayEnvironmentTagEditFields(tag)
        page.environmentTagInput(tag).value() == tag
    }

    void "environment tag edit options removed or hidden when click outside of button group"() {
        given: "add a tag"
        def tag = "Env Edit"
        def page = browser.page(EditProjectPage)
        page.addEnvironmentTag(tag)

        and: "edit tag"
        page.displayEnvironmentTagEditOptions(tag)

        when: "click outside input"
        page.environmentInput.click()

        then:
        page.environmentTagEditButton(tag).size() == 0

        and:
        page.environmentTagRemoveButton(tag).size() == 0
    }
}
