package com.manager.quality.everest.test.ui.functional.specs.scenario.edit

import com.manager.quality.everest.domains.Environment
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
class EditPageEnvironmentsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "environment select defaults with multiple selected environment"() {
        setup: "project & scenario instances with environments"
        def env = new Environment(DataFactory.environment())
        def env1 = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def p = new Project(name: pd.name, code: pd.code, environments: [env, env1])
        def project = projectService.save(p)
        def sd = DataFactory.scenario()
        def scn = new Scenario(person: person, name: sd.name, executionMethod: sd.executionMethod,
                type: sd.type, project: project, environments: [env, env1])
        def scenario = scenarioService.save(scn)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "bug.environment is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.environmentsSelect().selectedText.containsAll(env.name, env1.name)
    }

    void "environment select defaults no selection when no environment set"() {
        setup: "project & scenario instances with environments"
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def sd = DataFactory.scenario()
        def scn = new Scenario(person: person, name: sd.name, executionMethod: sd.executionMethod,
                type: sd.type, project: project)
        def scenario = scenarioService.save(scn)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "environment defaults with no selection"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.environmentsSelect().selected.empty
    }

    void "environment options equal project environments"() {
        setup: "project & scenario instances with platforms"
        def env = new Environment(name: "env testing platform123")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def scenario = scenarioService.save(new Scenario(name: "platform testing scenario I", project: project,
                person: person, executionMethod: "Automated", type: "UI", environments: [env]))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/scenario/edit/${scenario.id}"

        then: "scenario.area is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        def found = project.environments*.name
        found.add(0, "No Environment...")
        found.containsAll(page.environmentsOptions*.text())
    }
}
