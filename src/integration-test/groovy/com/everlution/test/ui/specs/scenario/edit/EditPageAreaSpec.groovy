package com.everlution.test.ui.specs.scenario.edit

import com.everlution.Area
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageAreaSpec extends GebSpec {

    ProjectService projectService
    ScenarioService scenarioService

    void "area select populates with only elements within the associated project"() {
        setup: "project & scenario instances with areas"
        def area = new Area(name: "area testing area")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def scenario = scenarioService.save(new Scenario(name: "area testing scenario", project: project,
                creator: "testing", executionMethod: "Automated", type: "UI"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/scenario/edit/${scenario.id}"

        then: "area populates with project.areas"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.areaOptions*.text() == ["", area.name]
    }

    void "area select defaults with selected area"() {
        setup: "project & scenario instances with areas"
        def area = new Area(name: "area testing area")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def scenario = scenarioService.save(new Scenario(name: "area testing scenario I", project: project,
                creator: "testing", executionMethod: "Automated", type: "UI", area: area))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.areaSelect().selectedText == area.name
    }

    void "area select defaults empty text when no area set"() {
        setup: "project & scenario instances with areas"
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def scenario = scenarioService.save(new Scenario(name: "area testing scenario II", project: project,
                creator: "testing", executionMethod: "Automated", type: "UI"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.areaSelect().selectedText == ""
    }
}