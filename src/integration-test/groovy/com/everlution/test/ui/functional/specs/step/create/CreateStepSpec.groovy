package com.everlution.test.ui.functional.specs.step.create

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.StepService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.step.CreateStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateStepSpec extends GebSpec {

    ProjectService projectService
    StepService stepService

    void "authorized users can create step"(String username, String password) {
        given: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Step")

        when: "create a step"
        def page = at CreateStepPage
        page.createStep()

        then: "at show page"
        at ShowStepPage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all create form data saved"() {
        setup: "get fake data"
        def step = DataFactory.step()
        Project project = projectService.list(max: 1).first()
        Step linked = stepService.findAllByProject(project, [max:1]).results.first()

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        to (CreateStepPage, project.id)

        when: "create bug"
        def createPage = browser.page(CreateStepPage)
        createPage.createStepWithLink(step.action, step.name, step.result, linked.name, 'Is Sibling of')

        then: "data is displayed on show page"
        def showPage = at ShowStepPage
        verifyAll {
            showPage.nameValue.text() == step.name
            showPage.actionValue.text() == step.action
            showPage.resultValue.text() == step.result
            showPage.isLinkDisplayed(linked.name, 'siblings')
        }
    }

    void "link added to related step"() {
        setup: "get fake data"
        def step = DataFactory.step()
        Project project = projectService.list(max: 1).first()
        Step linked = stepService.findAllByProject(project, [max:1]).results.first()

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "create"
        def createPage = to (CreateStepPage, project.id)
        createPage.createStepWithLink(step.action, step.name, step.result, linked.name, 'Is Sibling of')

        then: "data is displayed on show page"
        def showPage = to (ShowStepPage, project.id, linked.id)
        showPage.isLinkDisplayed(step.name, 'siblings')
    }
}
