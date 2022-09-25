package com.everlution.test.ui.functional.specs.scenario.create

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageAreaSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenario/create"
    }

    void "area field has no value set"() {
        when: "project is selected"
        def page = browser.page(CreateScenarioPage)

        then: "default text selected"
        page.areaSelect().selectedText == ""
        page.areaSelect().selected == ""
    }
}
