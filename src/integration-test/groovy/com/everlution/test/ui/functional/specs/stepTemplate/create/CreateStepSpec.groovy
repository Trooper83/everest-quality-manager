package com.everlution.test.ui.functional.specs.stepTemplate.create

import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.services.steptemplate.StepTemplateService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.stepTemplate.CreateStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ShowStepTemplatePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreateStepSpec extends GebSpec {

    ProjectService projectService
    StepTemplateService stepTemplateService

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
        projectHomePage.sideBar.goToCreate("Step Template")

        when: "create a step"
        def page = at CreateStepTemplatePage
        page.createStepTemplate()

        then: "at show page"
        at ShowStepTemplatePage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all create form data saved"() {
        setup: "get fake data"
        def step = DataFactory.step()
        Project project = projectService.list(max: 1).first()
        def linked = stepTemplateService.findAllInProject(project, [max:1]).results.first()

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        to (CreateStepTemplatePage, project.id)

        when: "create bug"
        def createPage = browser.page(CreateStepTemplatePage)
        createPage.createStepTemplateWithLink(step.action, step.name, step.result, linked.name, 'Is Sibling of')

        then: "data is displayed on show page"
        def showPage = at ShowStepTemplatePage
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
        def linked = stepTemplateService.findAllInProject(project, [max:1]).results.first()

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "create"
        def createPage = to (CreateStepTemplatePage, project.id)
        createPage.createStepTemplateWithLink(step.action, step.name, step.result, linked.name, 'Is Sibling of')

        then: "data is displayed on show page"
        def showPage = to (ShowStepTemplatePage, project.id, linked.id)
        showPage.isLinkDisplayed(step.name, 'siblings')
    }
}
