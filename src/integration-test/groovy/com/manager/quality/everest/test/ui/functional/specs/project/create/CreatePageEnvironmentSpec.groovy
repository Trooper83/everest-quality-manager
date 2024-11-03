package com.manager.quality.everest.test.ui.functional.specs.project.create

import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.CreateProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ShowProjectPage
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

    void "create environment input tooltip displayed when blank or whitespace"(String text) {
        when: "add a blank tag name"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag(text)

        then: "tooltip is displayed"
        page.getToolTipText() == "Environment Name cannot be blank"

        where:
        text << ['', ' ']
    }

    void "removed env tags do not persist"() {
        given: "add two tags"
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag("Test Env1")
        page.addEnvironmentTag("Test Env2")

        when: "remove one tag"
        page.removeEnvironmentTag("Test Env1")

        and:
        page.addEnvironmentTag("Test Env3")

        and:
        def p = DataFactory.project()
        page.createProject(p.name, p.code)

        then: "only the selected tag is removed"
        def show = browser.page(ShowProjectPage)
        !show.isEnvironmentDisplayed("Test Env1")
        show.isEnvironmentDisplayed("Test Env2")
        show.isEnvironmentDisplayed("Test Env3")
    }

    void "adding tag sets input focus"() {
        when:
        def page = browser.page(CreateProjectPage)
        page.addEnvironmentTag('tag')

        then:
        page.environmentInput.focused
    }
}
