package com.manager.quality.everest.test.ui.functional.specs.scenario.edit

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.Scenario
import com.manager.quality.everest.services.scenario.ScenarioService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.scenario.EditScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class EditPageAreaSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "area select defaults with selected area"() {
        setup: "project & scenario instances with areas"
        def area = new Area(name: "area testing area")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def scenario = scenarioService.save(new Scenario(name: "area testing scenario I", project: project,
                person: person, executionMethod: "Automated", type: "UI", area: area))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.areaSelect().selectedText == area.name
    }

    void "area select defaults empty text when no area set"() {
        setup: "project & scenario instances with areas"
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def scenario = scenarioService.save(new Scenario(name: "area testing scenario II", project: project,
                person: person, executionMethod: "Automated", type: "UI"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.areaSelect().selectedText == ""
    }

    void "area options equal project areas"() {
        setup: "project & scenario instances with areas"
        def area = new Area(name: "area testing area123")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def scenario = scenarioService.save(new Scenario(name: "area testing scenario I", project: project,
                person: person, executionMethod: "Automated", type: "UI", area: area))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        def found = project.areas*.name
        found.add(0, "")
        found.containsAll(page.areaOptions*.text())
    }
}
