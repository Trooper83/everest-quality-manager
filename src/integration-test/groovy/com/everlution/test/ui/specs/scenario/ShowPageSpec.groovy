package com.everlution.test.ui.specs.scenario

import com.everlution.ScenarioService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class ShowPageSpec extends GebSpec {

    ScenarioService scenarioService

    @Shared int id

    def setup() {
        id = scenarioService.list(max: 1).first().id
    }

    void "status message displayed after scenario created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        def page = to CreateScenarioPage

        when: "create scenario"
        page.createScenario()

        then: "at show page with message displayed"
        def showPage = at ShowScenarioPage
        showPage.statusMessage.text() ==~ /Scenario \d+ created/
    }

    void "home link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to scenario"
        go "/scenario/show/${id}"

        when: "click the home button"
        def page = browser.page(ShowScenarioPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to scenario"
        go "/scenario/show/${id}"

        when: "click the list link"
        def page = browser.page(ShowScenarioPage)
        page.goToList()

        then: "at the list page"
        at ListScenarioPage
    }

    void "create link directs to create view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to scenario"
        go "/scenario/show/${id}"

        when: "click the new bug link"
        def page = browser.page(ShowScenarioPage)
        page.goToCreate()

        then: "at the create page"
        at CreateScenarioPage
    }

    void "edit link directs to edit view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to scenario"
        go "/scenario/show/${id}"

        when: "click the edit button"
        def page = browser.page(ShowScenarioPage)
        page.goToEdit()

        then: "at the edit page"
        at EditScenarioPage
    }

    void "create delete edit buttons not displayed for Read Only user"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to scenario"
        go "/scenario/show/${id}"

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowScenarioPage)
        verifyAll {
            !page.createLink.displayed
            !page.deleteLink.displayed
            !page.editLink.displayed
        }
    }

    void "create delete edit buttons displayed for authorized users"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to scenario"
        go "/scenario/show/${id}"

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowScenarioPage)
        verifyAll {
            page.createLink.displayed
            page.deleteLink.displayed
            page.editLink.displayed
        }

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "correct fields are displayed"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to scenario"
        go "/scenario/show/${id}"

        then: "correct fields are displayed"
        def page = browser.page(ShowScenarioPage)
        page.getFields() == ["Creator", "Project", "Area", "Environments", "Name", "Description",
                             "Execution Method", "Type", "Gherkin"]
    }

    void "scenario not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to scenario"
        go "/scenario/show/${id}"

        when: "click delete and cancel | verify message"
        def showPage = browser.page(ShowScenarioPage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show page"
        at ShowScenarioPage
    }

    void "updated message displays after updating scenario"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit scenario"
        go "/scenario/edit/${id}"

        when: "edit a scenario"
        def page = browser.page(EditScenarioPage)
        page.editScenario()

        then: "at show scenario page with message displayed"
        def showPage = at ShowScenarioPage
        showPage.statusMessage.text() == "Scenario ${id} updated"
    }
}
