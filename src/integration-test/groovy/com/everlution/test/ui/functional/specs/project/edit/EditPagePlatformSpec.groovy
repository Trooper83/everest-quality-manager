package com.everlution.test.ui.functional.specs.project.edit

import com.everlution.services.project.ProjectService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class EditPagePlatformSpec extends GebSpec {

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

    void "platform tag is added and removed"() {
        given: "add a tag"
        def tag = "Test Platform"
        def page = browser.page(EditProjectPage)
        page.addPlatformTag(tag)

        expect: "tag is displayed"
        page.isPlatformTagDisplayed(tag)

        when: "remove tag"
        page.removePlatformTag(tag)

        then: "tag is not displayed"
        !page.isPlatformTagDisplayed(tag)
    }

    void "platform tag input is cleared when tag added"() {
        given: "add a tag"
        def tag = "Test Platform"
        def page = browser.page(EditProjectPage)
        page.addPlatformTag(tag)

        expect: "tag is displayed and input is cleared"
        page.isPlatformTagDisplayed(tag)
        page.platformInput.value() == ""
    }

    void "hidden platform input is added"() {
        given: "add a tag"
        def page = browser.page(EditProjectPage)
        page.addPlatformTag("Test Platform")

        expect: "hidden input is present and not displayed"
        !page.isPlatformTagHiddenInputDisplayed("Test Platform")
    }

    void "removing one tag does not remove all"() {
        given: "add two tags"
        def page = browser.page(EditProjectPage)
        page.addPlatformTag("Test Platform1")
        page.addPlatformTag("Test Platform2")

        expect: "tag is found"
        page.isPlatformTagDisplayed("Test Platform2")

        when: "remove one tag"
        page.removePlatformTag("Test Platform1")

        then: "only the selected tag is removed"
        page.isPlatformTagDisplayed("Test Platform2")
    }

    void "create platform input tooltip text"() {
        when: "add a blank tag name"
        def page = browser.page(EditProjectPage)
        page.addPlatformTag("")

        then: "tooltip is displayed"
        page.getToolTipText() == "Platform Name cannot be blank"
    }
}
