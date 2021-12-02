package com.everlution.test.ui.specs.scenario

import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    ProjectService projectService
    ScenarioService scenarioService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list page"
        to ListScenarioPage

        then: "correct headers are displayed"
        ListScenarioPage page = browser.page(ListScenarioPage)
        page.scenarioTable.getHeaders() == ["Name", "Creator", "Type", "Execution Method", "Project"]
    }

    void "home link directs to home view"() {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list scenario page"
        to ListScenarioPage

        when: "click home button"
        ListScenarioPage page = browser.page(ListScenarioPage)
        page.goToHome()

        then: "at home page"
        at HomePage
    }

    void "new scenario link directs to create view"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list scenario page"
        to ListScenarioPage

        when: "go to create scenario page"
        ListScenarioPage page = browser.page(ListScenarioPage)
        page.goToCreateScenario()

        then: "at scenario page"
        at CreateScenarioPage
    }

    void "create button not displayed on list for Read Only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list scenario page"
        to ListScenarioPage

        then: "create scenario button is not displayed"
        ListScenarioPage page = browser.page(ListScenarioPage)
        !page.createScenarioLink.displayed
    }

    void "create button displayed on list for users"(String username, String password) {
        given: "login as basic and above user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        when: "go to list scenario page"
        to ListScenarioPage

        then: "create scenario button is displayed"
        ListScenarioPage page = browser.page(ListScenarioPage)
        page.createScenarioLink.displayed

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "delete message displays after scenario deleted"() {
        given: "scenario"
        def project = projectService.list(max: 1).first()
        def sd = DataFactory.scenario()
        def scn = new Scenario(creator: sd.creator, name: sd.name, executionMethod: "Manual", type: "UI",
                description: sd.description, project: project)
        def id = scenarioService.save(scn).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to scenario"
        go "/scenario/show/${id}"

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
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list page"
        def listPage = to ListScenarioPage

        when: "click first scenario in list"
        listPage.scenarioTable.clickCell("Name", 0)

        then: "at show page"
        at ShowScenarioPage
    }
}
