package com.everlution.test.ui.functional.specs.scenario.create

import com.everlution.domains.Environment
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePageEnvironmentsSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        setup: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
    }

    void "options equal project environments"() {
        setup:
        def env = new Environment(name: "env testing platform123")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def page = to CreateScenarioPage, project.id

        expect: "default text selected"
        page.environmentsSelect().selectedText == []
        page.environmentsSelect().selected == []
        def found = project.environments*.name
        found.add(0, "Select Environments...")
        found.containsAll(page.environmentsOptions*.text())
    }
}
