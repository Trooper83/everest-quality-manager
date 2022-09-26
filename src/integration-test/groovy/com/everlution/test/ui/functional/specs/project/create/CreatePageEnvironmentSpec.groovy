package com.everlution.test.ui.functional.specs.project.create

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageEnvironmentSpec extends GebSpec {

    def setup() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to the create page"
        to CreateProjectPage
    }

    void "environment tag is added and removed"() {
        given: "add a tag"
        def tag = "Test Env"
        def page = browser.page(CreateProjectPage)
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
        def tag = "Test Env"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag(tag)

        expect: "tag is displayed and input is cleared"
        page.isEnvironmentTagDisplayed(tag)
        page.environmentInput.value() == ""
    }

    void "hidden environment input is added"() {
        given: "add a tag"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag("Test Env")

        expect: "hidden input is present and not displayed"
        !page.isEnvironmentTagHiddenInputDisplayed("Test Env")
    }

    void "removing one tag does not remove all"() {
        given: "add two tags"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag("first")
        page.addEnvironmentTag("second")

        expect: "two tags are found"
        page.environmentTags.size() == 2
        page.isEnvironmentTagDisplayed("second")

        when: "remove one tag"
        page.removeEnvironmentTag("first")

        then: "only the selected tag is removed"
        page.environmentTags.size() == 1
        page.isEnvironmentTagDisplayed("second")
    }

    void "environment tag can be edited"() {
        given: "add a tag"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag("Env Edit")

        when: "edit tag"
        page.editEnvironmentTag("Env Edit", "Edit Env")

        then: "tag is updated"
        page.isEnvironmentTagDisplayed("Edit Env")
        !page.isEnvironmentTagDisplayed("Env Edit")
    }

    void "environment tag elements correct after tag edited"() {
        given: "add a tag"
        def tag = "Env Edit"
        def edited = "Edit Env"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag(tag)

        when: "edit tag"
        page.editEnvironmentTag(tag, edited)

        then: "save button removed"
        page.environmentTagSaveButton(edited).size() == 0

        and: "input is hidden"
        !page.isEnvironmentTagHiddenInputDisplayed(edited)

        and: "edit button is not displayed"
        page.environmentTagEditButton(edited).size() == 0

        and: "delete button is not displayed"
        page.environmentTagRemoveButton(edited).size() == 0
    }

    void "create environment input tooltip displayed when blank or whitespace"(String text) {
        when: "add a blank tag name"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag(text)

        then: "tooltip is displayed"
        page.getToolTipText() == "Environment Name cannot be blank"

        where:
        text << ['', ' ']
    }

    void "edit environment name input tooltip displayed when empty"(String text) {
        given: "add tag with name"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag("Edit Env")

        when: "edit tag with blank value"
        page.editEnvironmentTag("Edit Env", text)

        then: "tooltip displayed"
        page.getToolTipText() == "Environment Name cannot be blank"

        where:
        text << ['', ' ']
    }

    void "environment tag edit fields removed or hidden when cancelled"() {
        given: "add a tag"
        def tag = "Env Edit"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag(tag)

        and: "edit tag"
        page.displayEnvironmentTagEditFields(tag)

        when: "cancel edit"
        page.cancelEnvironmentTagEdit(tag)

        then: "save button removed"
        page.environmentTagSaveButton(tag).size() == 0

        and: "input is hidden"
        !page.isEnvironmentTagHiddenInputDisplayed(tag)

        and: "edit button is not displayed"
        page.environmentTagEditButton(tag).size() == 0

        and: "delete button is not displayed"
        page.environmentTagEditButton(tag).size() == 0
    }

    void "environment tag edit fields removed or hidden when click outside of input field"() {
        given: "add a tag"
        def tag = "Env Edit"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag(tag)

        and: "edit tag"
        page.displayEnvironmentTagEditFields(tag)

        when: "click outside input"
        page.environmentInput.click()

        then: "save button removed"
        page.environmentTagSaveButton(tag).size() == 0

        and: "input is hidden"
        !page.isEnvironmentTagHiddenInputDisplayed(tag)

        and: "edit button is not displayed"
        page.environmentTagEditButton(tag).size() == 0

        and: "delete button is not displayed"
        page.environmentTagRemoveButton(tag).size() == 0
    }

    void "environment tag edit fields removed or hidden when second tag is edited"() {
        given: "add a tag"
        def tag = "Edit Env"
        def tag1 = "Tag"
        def page = browser.page(CreateProjectPage)
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
        def tag = "Env Edit"
        def page = browser.page(CreateProjectPage)
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
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag(tag)

        and: "edit tag"
        page.displayEnvironmentTagEditOptions(tag)

        when: "click outside input"
        page.environmentInput.click()

        then: "edit button is displayed"
        page.environmentTagEditButton(tag).size() == 0

        and: "delete button is displayed"
        page.environmentTagRemoveButton(tag).size() == 0
    }
}
