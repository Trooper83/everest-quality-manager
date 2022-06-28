package com.everlution.test.ui.specs.scenario.edit

import com.everlution.Area
import com.everlution.Environment
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditScenarioSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService

    void "authorized users can edit scenario"(String username, String password) {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        def id = scenarioService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/project/${project.id}/scenario/edit/${id}"

        when: "edit the test case"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.editScenario()

        then: "at show test case page with message displayed"
        at ShowScenarioPage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all edit from data saved"() {
        setup: "get fake data"
        def area = new Area(DataFactory.area())
        def env = new Environment(DataFactory.environment())
        def projectData = DataFactory.project()
        def person = personService.list(max: 1).first()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code,
                areas: [area], environments: [env]))
        def sd = DataFactory.scenario()
        def scenario = new Scenario(name: sd.name, description: sd.description, person: person, project: project,
                area: area, executionMethod: "Manual", type: "API", platform: "Web", environments: [env])
        def id = scenarioService.save(scenario).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/scenario/edit/${id}"

        when: "edit test case"
        EditScenarioPage page = browser.page(EditScenarioPage)
        def edited = DataFactory.scenario()
        page.editScenario(edited.name, edited.description, edited.gherkin, "", [""],
                "Automated", "UI", "iOS")

        then: "data is displayed on show page"
        ShowScenarioPage showPage = at ShowScenarioPage
        verifyAll {
            showPage.areaValue.text() == ""
            !showPage.areEnvironmentsDisplayed([env.name])
            showPage.projectValue.text() == project.name
            showPage.nameValue.text() == edited.name
            showPage.descriptionValue.text() == edited.description
            showPage.executionMethodValue.text() == "Automated"
            showPage.platformValue.text() == "iOS"
            showPage.typeValue.text() == "UI"
            showPage.gherkinTextArea.text() == edited.gherkin
        }
    }
}
