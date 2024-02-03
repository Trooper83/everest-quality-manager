package com.everlution.test.ui.functional.specs.scenario.edit

import com.everlution.Environment
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

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
}
