package com.everlution.test.ui.specs.scenario.create

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageAreaSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        to CreateScenarioPage
    }

    void "area field has no value set"() {
        when: "project is selected"
        def page = browser.page(CreateScenarioPage)
        page.projectSelect().selected = "1"

        then: "default text selected"
        page.areaSelect().selectedText == "Select an Area..."
        page.areaSelect().selected == ""
    }

    void "area field defaults disabled"() {
        expect: "area is disabled"
        def page = browser.page(CreateScenarioPage)
        page.areaSelect().disabled
    }

    void "area field disabled and depopulated when project is set to default"() {
        given: "project is selected"
        def page = browser.page(CreateScenarioPage)
        page.projectSelect().selected = "bootstrap project"

        expect: "area field enabled and populated"
        waitFor() { //need to wait for transition
            !page.areaSelect().disabled
            page.areaOptions.size() == 3
        }

        when: "project set to default"
        page.projectSelect().selected = ""

        then: "area is disabled, depopulated and set to default"
        waitFor() { //need to wait for transition
            page.areaSelect().disabled
            page.areaOptions.size() == 1
            page.areaSelect().selectedText == "Select an Area..."
            page.areaSelect().selected == ""
        }
    }
}
