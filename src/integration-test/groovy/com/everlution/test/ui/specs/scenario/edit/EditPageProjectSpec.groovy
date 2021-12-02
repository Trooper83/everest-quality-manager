package com.everlution.test.ui.specs.scenario.edit

import com.everlution.ScenarioService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageProjectSpec extends GebSpec {

    ScenarioService scenarioService
    @Shared int id

    def setup() {
        id = scenarioService.list(max:1).first().id
    }

    void "project name tag is html span and project cannot be edited"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/scenario/edit/${id}"

        then: "project tag is span"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.projectNameField.tag() == "span"
    }
}
