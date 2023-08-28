package com.everlution.test.ui.functional.specs.step

import com.everlution.PersonService
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.StepService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.step.EditStepPage
import com.everlution.test.ui.support.pages.step.ListStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepService stepService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when: "go to list page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/steps"

        then: "correct headers are displayed"
        ListScenarioPage page = browser.page(ListScenarioPage)
        page.listTable.getHeaders() == ["Name", "Action", "Result", "Created By"]
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/steps"

        when: "click first scenario in list"
        def listPage = browser.page(ListStepPage)
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowStepPage
    }

    void "search returns results and text retained in search box"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def projectPage = at ProjectHomePage
        projectPage.sideBar.goToProjectDomain('Steps')

        when:
        def page = at ListStepPage
        page.search('step')

        then:
        page.listTable.rowCount > 0
        page.nameInput.text == 'step'
    }

    void "reset button reloads results"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def projectPage = at ProjectHomePage
        projectPage.sideBar.goToProjectDomain('Steps')

        and:
        def page = at ListStepPage
        page.search('step')

        expect:
        page.listTable.rowCount > 0
        page.nameInput.text == 'step'

        when:
        page.resetSearch()

        then:
        page.listTable.rowCount > 0
        page.nameInput.text == ''
    }

    void "delete message displays after scenario deleted"() {
        given:
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def step = new Step(name: 'testing', person: person, project: project, act: 'action', result: 'result',
                isBuilderStep: true)
        def id = stepService.save(step).id

        and: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to list page"
        go "/project/${project.id}/step/show/${id}"

        when:
        browser.page(ShowStepPage).delete()

        then: "at show page"
        def steps = at ListStepPage
        steps.statusMessage.displayed
    }
}
