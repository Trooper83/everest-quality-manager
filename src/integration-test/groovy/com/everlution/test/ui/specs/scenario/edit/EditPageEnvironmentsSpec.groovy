package com.everlution.test.ui.specs.scenario.edit

import com.everlution.Environment
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
class EditPageEnvironmentsSpec extends GebSpec {

    ProjectService projectService
    ScenarioService scenarioService

    void "environment select populates with only elements within the associated project"() {
        setup: "project & scenario instances with environments"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def sd = DataFactory.scenario()
        def scn = new Scenario(creator: sd.creator, name: sd.name, executionMethod: sd.executionMethod,
                type: sd.type, project: project, environments: [env])
        def scenario = scenarioService.save(scn)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/scenario/edit/${scenario.id}"

        then: "environment populates with project.environments"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.environmentsOptions*.text() == ["--No Environment--", env.name]
    }

    void "environment select defaults with multiple selected environment"() {
        setup: "project & scenario instances with environments"
        def env = new Environment(DataFactory.environment())
        def env1 = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def p = new Project(name: pd.name, code: pd.code, environments: [env, env1])
        def project = projectService.save(p)
        def sd = DataFactory.scenario()
        def scn = new Scenario(creator: sd.creator, name: sd.name, executionMethod: sd.executionMethod,
                type: sd.type, project: project, environments: [env, env1])
        def scenario = scenarioService.save(scn)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/scenario/edit/${scenario.id}"

        then: "bug.environment is selected"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.environmentsSelect().selectedText == [env.name, env1.name]
    }

    void "environment select defaults no selection when no environment set"() {
        setup: "project & scenario instances with environments"
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def sd = DataFactory.scenario()
        def scn = new Scenario(creator: sd.creator, name: sd.name, executionMethod: sd.executionMethod,
                type: sd.type, project: project)
        def scenario = scenarioService.save(scn)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/scenario/edit/${scenario.id}"

        then: "environment defaults with no selection"
        EditScenarioPage page = browser.page(EditScenarioPage)
        page.environmentsSelect().selected.empty
    }
}
