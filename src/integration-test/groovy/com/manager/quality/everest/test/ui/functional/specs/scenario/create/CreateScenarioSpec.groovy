package com.manager.quality.everest.test.ui.functional.specs.scenario.create

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.scenario.CreateScenarioPage
import com.manager.quality.everest.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreateScenarioSpec extends GebSpec {

    ProjectService projectService

    void "authorized users can create scenario"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenario/create"

        when: "create a scenario"
        CreateScenarioPage page = browser.page(CreateScenarioPage)
        page.createScenario()

        then: "at show page"
        at ShowScenarioPage

        where:
        username                        | password
        Credentials.BASIC.email | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all create form data saved"() {
        setup: "get fake data"
        def area = new Area(DataFactory.area())
        def platform = new Platform(DataFactory.area())
        def ed = DataFactory.environment()
        def ed1 = DataFactory.environment()
        def env = new Environment(ed)
        def env1 = new Environment(ed1)
        def projectData = DataFactory.project()
        def project = projectService.save(
                new Project(name: projectData.name, code: projectData.code, areas: [area], environments: [env, env1],
                platforms: [platform])
        )

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "create test case"
        go "/project/${project.id}/scenario/create"
        def createPage = browser.page(CreateScenarioPage)
        def scn = DataFactory.scenario()
        createPage.createScenario(scn.name, scn.description, scn.gherkin, area.name,
                [env.name, env1.name],"Automated", "UI", platform.name)

        then: "data is displayed on show page"
        def showPage = at ShowScenarioPage
        verifyAll {
            showPage.areaValue.text() == area.name
            showPage.nameValue.text() == scn.name
            showPage.descriptionValue.text() == scn.description
            showPage.executionMethodValue.text() == "Automated"
            showPage.typeValue.text() == "UI"
            showPage.platformValue.text() == platform.name
            showPage.areEnvironmentsDisplayed([env.name, env1.name])
            showPage.gherkinTextArea.text() == scn.gherkin
        }
    }
}
