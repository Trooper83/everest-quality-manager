package com.everlution.test.ui.specs.scenario.edit

import com.everlution.Area
import com.everlution.Environment
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditScenarioSpec extends GebSpec {

    ProjectService projectService
    ScenarioService scenarioService

    void "authorized users can edit test case"(String username, String password) {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        def id = scenarioService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/scenario/edit/${id}"

        when: "edit the test case"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.editScenario()

        then: "at show test case page with message displayed"
        at ShowScenarioPage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "all edit from data saved"() {
        setup: "get fake data"
        def area = new Area(DataFactory.area())
        def env = new Environment(DataFactory.environment())
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code,
                areas: [area], environments: [env]))
        def sd = DataFactory.scenario()
        def scenario = new Scenario(name: sd.name, description: sd.description, creator: sd.creator, project: project,
                area: area, executionMethod: "Manual", type: "API", environments: [env])
        def id = scenarioService.save(scenario).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/scenario/edit/${id}"

        when: "edit test case"
        EditScenarioPage page = browser.page(EditScenarioPage)
        def edited = DataFactory.scenario()
        page.editScenario(edited.name, edited.description, edited.gherkin, "", [""], "Automated", "UI")

        then: "data is displayed on show page"
        ShowScenarioPage showPage = at ShowScenarioPage
        verifyAll {
            showPage.areaValue.text() == ""
            !showPage.areEnvironmentsDisplayed([env.name])
            showPage.projectValue.text() == project.name
            showPage.nameValue.text() == edited.name
            showPage.descriptionValue.text() == edited.description
            showPage.executionMethodValue.text() == "Automated"
            showPage.typeValue.text() == "UI"
            showPage.gherkinTextArea.text() == edited.gherkin
        }
    }
}
