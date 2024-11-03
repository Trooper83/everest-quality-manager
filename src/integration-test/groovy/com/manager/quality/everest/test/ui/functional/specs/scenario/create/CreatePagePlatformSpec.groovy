package com.manager.quality.everest.test.ui.functional.specs.scenario.create

import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.scenario.CreateScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePagePlatformSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
    }

    void "platform options equal project platforms"() {
        setup:
        def platform = new Platform(name: "platform testing platform123")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform]))
        def page = to CreateScenarioPage, project.id

        expect:
        page.platformSelect().selectedText == ""
        page.platformSelect().selected == ""
        def found = project.platforms*.name
        found.add(0, "")
        found.containsAll(page.platformOptions*.text())
    }
}
