package com.everlution.test.ui.specs.scenario.create

import com.everlution.*
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageEnvironmentsSpec extends GebSpec {

    ProjectService projectService
    ScenarioService scenarioService

    def setup() {
        setup: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")
    }

    void "environment select populates with only elements within the associated project"() {
        setup: "project & scenario instances with environments"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(
                new Project(name: pd.name, code: pd.code, environments: [env])
        )
        def sd = DataFactory.scenario()
        def scn = new Scenario(creator: sd.creator, name: sd.name, executionMethod: sd.executionMethod,
                type: sd.type, project: project, environments: [env])
        scenarioService.save(scn)

        and: "go to the create page"
        to CreateScenarioPage

        when: "select project"
        def page = browser.page(CreateScenarioPage)
        page.projectSelect().selected = project.name

        then: "environment populates with project.environments"
        waitFor() {
            page.environmentsSelect().enabled
        }
        page.environmentsOptions*.text() == ["--No Environment--", env.name]
    }

    void "environments field has no value set"() {
        given: "go to the create page"
        to CreateScenarioPage

        when: "project is selected"
        def page = browser.page(CreateScenarioPage)
        page.projectSelect().selected = "1"

        then: "default text selected"
        page.environmentsSelect().selectedText == []
        page.environmentsSelect().selected == []
    }

    void "environments field defaults with no environment option"() {
        given: "go to the create page"
        to CreateScenarioPage

        when: "project is selected"
        def page = browser.page(CreateScenarioPage)
        page.projectSelect().selected = "1"

        then: "default text present"
        page.environmentsOptions[0].text() == "--No Environment--"
    }

    void "environment field defaults disabled"() {
        given: "go to the create page"
        to CreateScenarioPage

        expect: "environment is disabled"
        def page = browser.page(CreateScenarioPage)
        page.environmentsSelect().disabled
    }

    void "environments field disabled and depopulated when project is set to default"() {
        given: "go to the create page"
        to CreateScenarioPage

        and: "project is selected"
        def page = browser.page(CreateScenarioPage)
        page.projectSelect().selected = "bootstrap project"

        expect: "environments field enabled and populated"
        waitFor() { //need to wait for transition
            !page.environmentsSelect().disabled
            page.environmentsOptions.size() == 3
        }

        when: "project set to default"
        page.projectSelect().selected = ""

        then: "environments is disabled, depopulated and set to default"
        waitFor(2) { //need to wait for transition
            page.environmentsSelect().disabled
            page.environmentsOptions*.text() == ["--No Environment--"]
            page.environmentsSelect().selectedText == []
            page.environmentsSelect().selected == []
        }
    }
}
