package com.manager.quality.everest.test.ui.functional.specs.scenario

import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.Scenario
import com.manager.quality.everest.services.scenario.ScenarioService
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.scenario.ListScenarioPage
import com.manager.quality.everest.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class DeleteScenarioSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService

    void "authorized users can delete scenario"(String username, String password) {
        given:
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def scn = new Scenario(name: "delete scenario", person: person, project: project,
                executionMethod: "Manual", type: "UI")
        def id = scenarioService.save(scn).id

        and: "log in as authorized user"
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/project/${project.id}/scenario/show/${id}"

        when: "delete scenario"
        def showPage = browser.at(ShowScenarioPage)
        showPage.delete()

        then: "at list page"
        at ListScenarioPage

        where:
        username                        | password
        Credentials.BASIC.email | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }
}
