package com.everlution.test.ui.functional.specs.scenario.create

import com.everlution.domains.Area
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
class CreatePageAreaSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
    }

    void "area field has no value set"() {
        setup:
        def area = new Area(name: "area testing platform123")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def page = to CreateScenarioPage, project.id

        expect: "default text selected"
        page.areaSelect().selectedText == ""
        page.areaSelect().selected == ""
        def found = project.areas*.name
        found.add(0, "")
        found.containsAll(page.areaOptions*.text())
    }
}
