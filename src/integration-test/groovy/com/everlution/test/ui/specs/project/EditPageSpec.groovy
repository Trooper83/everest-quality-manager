package com.everlution.test.ui.specs.project

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
        browser.page(EditProjectPage).getFields() == ["Name *", "Code *", "Areas"]
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
}
