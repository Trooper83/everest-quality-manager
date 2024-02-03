package com.everlution.test.ui.functional.specs.scenario.create

import com.everlution.*
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageEnvironmentsSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        setup: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenario/create"
    }

    void "environments field has no value set"() {
        when: "project is selected"
        def page = browser.page(CreateScenarioPage)

        then: "default text selected"
        page.environmentsSelect().selectedText == []
        page.environmentsSelect().selected == []
    }

    void "environments field defaults with no environment option"() {
        when: "project is selected"
        def page = browser.page(CreateScenarioPage)

        then: "default text present"
        page.environmentsOptions[0].text() == "Select Environments..."
    }
}
