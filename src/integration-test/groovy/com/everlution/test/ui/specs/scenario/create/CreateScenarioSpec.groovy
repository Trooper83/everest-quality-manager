package com.everlution.test.ui.specs.scenario.create

import com.everlution.Area
import com.everlution.Environment
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateScenarioSpec extends GebSpec {

    ProjectService projectService

    void "authorized users can create scenario"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        to CreateScenarioPage

        when: "create a scenario"
        CreateScenarioPage page = browser.page(CreateScenarioPage)
        page.createScenario()

        then: "at show page"
        at ShowScenarioPage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "all create form data saved"() {
        setup: "get fake data"
        def area = new Area(DataFactory.area())
        def ed = DataFactory.environment()
        def ed1 = DataFactory.environment()
        def env = new Environment(ed)
        def env1 = new Environment(ed1)
        def projectData = DataFactory.project()
        def project = projectService.save(
                new Project(name: projectData.name, code: projectData.code, areas: [area], environments: [env, env1])
        )

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "create test case"
        def createPage = to CreateScenarioPage
        def scn = DataFactory.scenario()
        createPage.createScenario(scn.name, scn.description, scn.gherkin, project.name, area.name,
                [env.name, env1.name],"Automated", "UI")

        then: "data is displayed on show page"
        def showPage = at ShowScenarioPage
        verifyAll {
            showPage.areaValue.text() == area.name
            showPage.projectValue.text() == project.name
            showPage.nameValue.text() == scn.name
            showPage.descriptionValue.text() == scn.description
            showPage.executionMethodValue.text() == "Automated"
            showPage.typeValue.text() == "UI"
            showPage.areEnvironmentsDisplayed([env.name, env1.name])
            showPage.gherkinTextArea.text() == scn.gherkin
        }
    }
}
