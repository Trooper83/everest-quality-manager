package com.everlution.test.ui.specs.scenario

import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class DeleteScenarioSpec extends GebSpec {

    ProjectService projectService
    ScenarioService scenarioService

    int id

    def setup() {
        def project = projectService.list(max: 1).first()
        def scn = new Scenario(name: "delete scenario", creator: "testing creator", project: project,
                executionMethod: "Manual", type: "UI")
        id = scenarioService.save(scn).id
    }

    void "authorized users can delete scenario"(String username, String password) {
        given: "log in as authorized user"
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/scenario/show/${id}"

        when: "delete scenario"
        def showPage = browser.at(ShowScenarioPage)
        showPage.deleteScenario()

        then: "at list page"
        at ListScenarioPage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }
}
