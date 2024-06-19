package com.everlution.test.ui.functional.specs.scenario.edit

import com.everlution.domains.Person
import com.everlution.domains.Platform
import com.everlution.domains.Project
import com.everlution.domains.Scenario
import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.services.scenario.ScenarioService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class EditPagePlatformSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "platform select defaults with selected platform"() {
        setup: "project & scenario instances with platforms"
        def platform = new Platform(name: "platform testing platform")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform]))
        def scenario = scenarioService.save(new Scenario(name: "platform testing scenario I", project: project,
                person: person, executionMethod: "Automated", type: "UI", platform: platform))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.platformSelect().selectedText == platform.name
    }

    void "platform select defaults empty text when no platform set"() {
        setup: "project & scenario instances with areas"
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def scenario = scenarioService.save(new Scenario(name: "platform testing scenario II", project: project,
                person: person, executionMethod: "Automated", type: "UI"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.platformSelect().selectedText == ""
    }

    void "platform options equal project platform"() {
        setup: "project & scenario instances with platforms"
        def platform = new Platform(name: "platform testing platform123")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform]))
        def scenario = scenarioService.save(new Scenario(name: "platform testing scenario I", project: project,
                person: person, executionMethod: "Automated", type: "UI", platform: platform))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        def found = project.platforms*.name
        found.add(0, "")
        found.containsAll(page.platformOptions*.text())
    }
}
