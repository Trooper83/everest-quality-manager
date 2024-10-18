package com.manager.quality.everest.test.ui.functional.specs.scenario.edit

import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.Scenario
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.services.scenario.ScenarioService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.ui.support.pages.scenario.EditScenarioPage
import com.manager.quality.everest.test.ui.support.pages.scenario.ListScenarioPage
import com.manager.quality.everest.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class EditPageSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
    }

    void "verify method and type field options"() {
        setup:
        browser.page(ListProjectPage).projectTable.clickCell("Name", 0)
        browser.page(ProjectHomePage).sideBar.goToProjectDomain("Scenarios")
        browser.page(ListScenarioPage).listTable.clickCell("Name", 0)
        browser.page(ShowScenarioPage).goToEdit()

        expect: "correct options populate for executionMethod and type"
        EditScenarioPage page = browser.page(EditScenarioPage)
        verifyAll {
            page.executionMethodOptions*.text() == ["", "Automated", "Manual"]
            page.typeOptions*.text() == ["", "API", "UI"]
        }
    }

    void "verify platform field options"() {
        given:
        def platform = new Platform(DataFactory.area())
        def projectData = DataFactory.project()
        def project = projectService.save(
                new Project(name: projectData.name, code: projectData.code, platforms: [platform]))
        def person = personService.list(max:1).first()
        def scn = scenarioService.save(new Scenario(name: "name of the scenario", project: project, person: person))

        when:
        def page = to EditScenarioPage, project.id, scn.id

        then: "correct options populate"
        page.platformOptions*.text() == ["", platform.name]
    }
}
