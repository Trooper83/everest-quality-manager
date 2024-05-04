package com.everlution.test.ui.functional.specs.project.create

import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePagePlatformSpec extends GebSpec {

    def setup() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to the create page"
        to CreateProjectPage
    }

    void "platform tag is added and removed"() {
        given: "add a tag"
        def tag = "Test Platform"
        def page = browser.page(CreateProjectPage)
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
        def page = browser.page(CreateProjectPage)
        page.addPlatformTag(tag)

        expect: "tag is displayed and input is cleared"
        page.isPlatformTagDisplayed(tag)
        page.platformInput.value() == ""
    }

    void "hidden platform input is added"() {
        given: "add a tag"
        def page = browser.page(CreateProjectPage)
        page.addPlatformTag("Test Platform")

        expect: "hidden input is present and not displayed"
        !page.isPlatformTagHiddenInputDisplayed("Test Platform")
    }

    void "removing one tag does not remove all"() {
        given: "add two tags"
        def page = browser.page(CreateProjectPage)
        page.addPlatformTag("Test Platform1")
        page.addPlatformTag("Test Platform2")

        expect: "two tags are found"
        page.platformTags.size() == 2
        page.isPlatformTagDisplayed("Test Platform2")

        when: "remove one tag"
        page.removePlatformTag("Test Platform1")

        then: "only the selected tag is removed"
        page.platformTags.size() == 1
        page.isPlatformTagDisplayed("Test Platform2")
    }

    void "removed platform tags do not persist"() {
        given: "add two tags"
        def page = browser.page(CreateProjectPage)
        page.addPlatformTag("Test Platform1")
        page.addPlatformTag("Test Platform2")

        when: "remove one tag"
        page.removePlatformTag("Test Platform1")

        and:
        page.addPlatformTag("Test Platform3")

        and:
        def p = DataFactory.project()
        page.createProject(p.name, p.code)

        then: "only the selected tag is removed"
        def show = browser.page(ShowProjectPage)
        !show.isPlatformDisplayed("Test Platform1")
        show.isPlatformDisplayed("Test Platform2")
        show.isPlatformDisplayed("Test Platform3")
    }

    void "create platform input tooltip text displayed when blank or whitespace"(String text) {
        when: "add a blank tag name"
        def page = browser.page(CreateProjectPage)
        page.addPlatformTag(text)

        then: "tooltip is displayed"
        page.getToolTipText() == "Platform Name cannot be blank"

        where:
        text << ['', ' ']
    }

    void "adding tag sets input focus"() {
        when:
        def page = browser.page(CreateProjectPage)
        page.addPlatformTag('Test')

        then:
        page.platformInput.focused
    }
}
