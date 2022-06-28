package com.everlution.test.ui.specs.scenario

import com.everlution.PersonService
import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when: "go to list page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenarios"

        then: "correct headers are displayed"
        ListScenarioPage page = browser.page(ListScenarioPage)
        page.scenarioTable.getHeaders() == ["Name", "Description", "Person", "Project", "Platform", "Type"]
    }

    void "delete message displays after scenario deleted"() {
        given: "scenario"
        def project = projectService.list(max: 1).first()
        def sd = DataFactory.scenario()
        def person = personService.list(max: 1).first()
        def scn = new Scenario(person: person, name: sd.name, executionMethod: "Manual", type: "UI",
                description: sd.description, project: project)
        def id = scenarioService.save(scn).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to scenario"
        go "project/${project.id}/scenario/show/${id}"

        when: "delete scenario"
        ShowScenarioPage showPage = browser.page(ShowScenarioPage)
        showPage.deleteScenario()

        then: "at list page and message displayed"
        def listPage = at ListScenarioPage
        listPage.statusMessage.text() ==~ /Scenario \d+ deleted/
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenarios"

        when: "click first scenario in list"
        def listPage = browser.page(ListScenarioPage)
        listPage.scenarioTable.clickCell("Name", 0)

        then: "at show page"
        at ShowScenarioPage
    }
}
