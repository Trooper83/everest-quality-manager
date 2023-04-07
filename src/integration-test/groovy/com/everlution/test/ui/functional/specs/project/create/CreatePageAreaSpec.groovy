package com.everlution.test.ui.functional.specs.project.create

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageAreaSpec extends GebSpec {

    def setup() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to the create page"
        to CreateProjectPage
    }

    void "area tag is added and removed"() {
        given: "add a tag"
        def tag = "Test Area"
        def page = browser.page(CreateProjectPage)
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
        def page = browser.page(CreateProjectPage)
        page.addAreaTag(tag)

        expect: "tag is displayed and input is cleared"
        page.isAreaTagDisplayed(tag)
        page.areaInput.value() == ""
    }

    void "hidden area input is added"() {
        given: "add a tag"
        def page = browser.page(CreateProjectPage)
        page.addAreaTag("Test Area")

        expect: "hidden input is present and not displayed"
        !page.isAreaTagHiddenInputDisplayed("Test Area")
    }

    void "removing one tag does not remove all"() {
        given: "add two tags"
        def page = browser.page(CreateProjectPage)
        page.addAreaTag("Test Area1")
        page.addAreaTag("Test Area2")

        expect: "two tags are found"
        page.areaTags.size() == 2
        page.isAreaTagDisplayed("Test Area2")

        when: "remove one tag"
        page.removeAreaTag("Test Area1")

        then: "only the selected tag is removed"
        page.areaTags.size() == 1
        page.isAreaTagDisplayed("Test Area2")
    }

    void "removed area tags do not persist"() {
        given: "add two tags"
        def page = browser.page(CreateProjectPage)
        page.addAreaTag("Test Area1")
        page.addAreaTag("Test Area2")

        when: "remove one tag"
        page.removeAreaTag("Test Area1")

        and:
        page.addAreaTag("Test Area3")

        and:
        def p = DataFactory.project()
        page.createProject(p.name, p.code)

        then: "only the selected tag is removed"
        def show = browser.page(ShowProjectPage)
        !show.isAreaDisplayed("Test Area1")
        show.isAreaDisplayed("Test Area2")
        show.isAreaDisplayed("Test Area3")
    }

    void "create area input tooltip text displayed when blank or whitespace"(String text) {
        when: "add a blank tag name"
        def page = browser.page(CreateProjectPage)
        page.addAreaTag(text)

        then: "tooltip is displayed"
        page.getToolTipText() == "Area Name cannot be blank"

        where:
        text << ['', ' ']
    }
}
