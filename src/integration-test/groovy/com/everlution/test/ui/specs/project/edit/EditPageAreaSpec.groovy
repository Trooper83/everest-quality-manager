package com.everlution.test.ui.specs.project.edit

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageAreaSpec extends GebSpec {

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

    void "area tag is added and removed"() {
        given: "add a tag"
        def tag = "Test Area"
        def page = browser.page(EditProjectPage)
        page.addAreaTag(tag)

        expect: "tag is displayed"
        page.isAreaTagDisplayed(tag)

        when: "remove tag"
        page.removeAreaTag(tag)

        then: "tag is not displayed"
        !page.isAreaTagDisplayed(tag)
    }

    void "area tag input is cleared when tag added"() {
        given: "add a tag"
        def tag = "Test Area"
        def page = browser.page(EditProjectPage)
        page.addAreaTag(tag)

        expect: "tag is displayed and input is cleared"
        page.isAreaTagDisplayed(tag)
        page.areaInput.value() == ""
    }

    void "hidden area input is added"() {
        given: "add a tag"
        def page = browser.page(EditProjectPage)
        page.addAreaTag("Test Area")

        expect: "hidden input is present and not displayed"
        !page.isAreaTagHiddenInputDisplayed("Test Area")
    }

    void "removing one tag does not remove all"() {
        given: "add two tags"
        def page = browser.page(EditProjectPage)
        page.addAreaTag("Test Area1")
        page.addAreaTag("Test Area2")

        expect: "tag is found"
        page.isAreaTagDisplayed("Test Area2")

        when: "remove one tag"
        page.removeAreaTag("Test Area1")

        then: "only the selected tag is removed"
        page.isAreaTagDisplayed("Test Area2")
    }

    void "area tag can be edited"() {
        given: "add a tag"
        def page = browser.page(EditProjectPage)
        page.addAreaTag("Test Area Edit")

        when: "edit tag"
        page.editAreaTag("Test Area Edit", "Edited Area Tag")

        then: "tag is updated"
        page.isAreaTagDisplayed("Edited Area Tag")
        !page.isAreaTagDisplayed("Test Area Edit")
    }

    void "area tag elements correct after tag edited"() {
        given: "add a tag"
        def tag = "Test Area Edit"
        def edited = "Edited Area Tag"
        def page = browser.page(EditProjectPage)
        page.addAreaTag(tag)

        when: "edit tag"
        page.editAreaTag(tag, edited)

        then: "save button removed"
        page.areaTagSaveButton(edited).size() == 0

        and: "input is hidden"
        !page.isAreaTagHiddenInputDisplayed(edited)

        and: "edit button is displayed"
        page.areaTagEditButton(edited).size() == 1

        and: "delete button is displayed"
        page.areaTagRemoveButton(edited).size() == 1
    }

    void "create area input tooltip text"() {
        when: "add a blank tag name"
        def page = browser.page(EditProjectPage)
        page.addAreaTag("")

        then: "tooltip is displayed"
        page.tooltip.text() == "Area Name cannot be blank"
    }

    void "edit area name input tooltip text"() {
        given: "add tag with name"
        def page = browser.page(EditProjectPage)
        page.addAreaTag("test")

        when: "edit tag with blank value"
        page.editAreaTag("test", "")

        then: "tooltip displayed"
        page.tooltip.text() == "Area Name cannot be blank"
    }

    void "area name cannot be null"() {
        when: "add a blank tag name"
        def page = browser.page(EditProjectPage)
        page.addAreaTag(" ")
        page.editProject()

        then: "message is displayed"
        page.errorMessages.text() ==
                "Property [name] of class [class com.everlution.Area] cannot be null"
    }

    void "area tag edit fields removed or hidden when cancelled"() {
        given: "add a tag"
        def tag = "Test Area Edit"
        def page = browser.page(EditProjectPage)
        page.addAreaTag(tag)

        and: "edit tag"
        page.displayAreaTagEditFields(tag)

        when: "cancel edit"
        page.cancelAreaTagEdit(tag)

        then: "save button removed"
        page.areaTagSaveButton(tag).size() == 0

        and: "input is hidden"
        !page.isAreaTagHiddenInputDisplayed(tag)

        and: "edit button is displayed"
        page.areaTagEditButton(tag).size() == 1

        and: "delete button is displayed"
        page.areaTagRemoveButton(tag).size() == 1
    }

    void "area tag edit fields removed or hidden when click outside of input field"() {
        given: "add a tag"
        def tag = "Test Area Edit"
        def page = browser.page(EditProjectPage)
        page.addAreaTag(tag)

        and: "edit tag"
        page.displayAreaTagEditFields(tag)

        when: "click outside input"
        page.environmentInput.click()

        then: "save button removed"
        page.areaTagSaveButton(tag).size() == 0

        and: "input is hidden"
        !page.isAreaTagHiddenInputDisplayed(tag)

        and: "edit button is displayed"
        page.areaTagEditButton(tag).size() == 1

        and: "delete button is displayed"
        page.areaTagRemoveButton(tag).size() == 1
    }

    void "area tag edit fields removed or hidden when second tag is edited"() {
        given: "add a tag"
        def tag = "Test Area Edit"
        def tag1 = "Second Test Area Edit"
        def page = browser.page(EditProjectPage)
        page.addAreaTag(tag)
        page.addAreaTag(tag1)

        when: "edit tag"
        page.displayAreaTagEditFields(tag)
        page.displayAreaTagEditFields(tag1)

        then: "save button removed"
        page.areaTagSaveButton(tag).size() == 0

        and: "input is hidden"
        !page.isAreaTagHiddenInputDisplayed(tag)

        and: "edit button is displayed"
        page.areaTagEditButton(tag).size() == 1

        and: "delete button is displayed"
        page.areaTagRemoveButton(tag).size() == 1
    }

    void "area tag edits not persisted when cancelled"() {
        given: "add a tag"
        def tag = "Test Area Edit"
        def page = browser.page(EditProjectPage)
        page.addAreaTag(tag)

        and: "edit tag"
        page.displayAreaTagEditFields(tag)

        and: "edit tag value"
        page.areaTagInput(tag).value("should not persist")

        when: "cancel edit"
        page.cancelAreaTagEdit(tag)

        then: "save button removed"
        page.displayAreaTagEditFields(tag)
        page.areaTagInput(tag).value() == tag
    }
}
