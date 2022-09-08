package com.everlution.test.ui.functional.specs.scenario

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ShowPageSpec extends GebSpec {

    ProjectService projectService

    void "status message displayed after scenario created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenario/create"

        when: "create scenario"
        def page = at CreateScenarioPage
        page.createScenario()

        then: "at show page with message displayed"
        def showPage = at ShowScenarioPage
        showPage.statusMessage.text() ==~ /Scenario \d+ created/
    }

    void "edit link directs to edit view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Scenarios')

        and: "click first in list"
        def listPage = browser.page(ListScenarioPage)
        listPage.listTable.clickCell("Name", 0)

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
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Scenarios')

        when: "click first in list"
        def listPage = browser.page(ListScenarioPage)
        listPage.listTable.clickCell("Name", 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowScenarioPage)
        verifyAll {
            !page.deleteLink.displayed
            !page.editLink.displayed
        }
    }

    void "create delete edit buttons displayed for authorized users"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Scenarios')

        when: "click first in list"
        def listPage = browser.page(ListScenarioPage)
        listPage.listTable.clickCell("Name", 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowScenarioPage)
        verifyAll {
            page.deleteLink.displayed
            page.editLink.displayed
        }

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "scenario not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Scenarios')

        and: "click first in list"
        def listPage = browser.page(ListScenarioPage)
        listPage.listTable.clickCell("Name", 0)

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
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Scenarios')

        and: "click first in list"
        def listPage = browser.page(ListScenarioPage)
        listPage.listTable.clickCell("Name", 0)

        and:
        browser.page(ShowScenarioPage).goToEdit()

        when: "edit a scenario"
        def page = browser.page(EditScenarioPage)
        page.editScenario()

        then: "at show scenario page with message displayed"
        def showPage = at ShowScenarioPage
        showPage.statusMessage.text() ==~ /Scenario \d+ updated/
    }
}
