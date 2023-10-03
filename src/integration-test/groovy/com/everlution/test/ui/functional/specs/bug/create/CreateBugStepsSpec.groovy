package com.everlution.test.ui.functional.specs.bug.create

import com.everlution.PersonService
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.StepService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateBugStepsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepService stepService

    void "removed free form test steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")

        and: "go to the create page"
        def page = at CreateBugPage

        when: "fill in create form"
        page.completeCreateForm()

        and: "add a new test step"
        page.scrollToBottom()
        page.stepsTable.selectStepsTab("free-form")
        page.stepsTable.addStep("should not persist", "should not persist")

        and: "remove row"
        page.stepsTable.removeRow(1)

        and: "submit form"
        page.createButton.click()

        then: "at show page"
        def showPage = at ShowBugPage
        showPage.getStepsCount() == 1
    }


    void "removed test builder steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/bug/create"

        when: "fill in create form"
        def page = browser.page(CreateBugPage)
        page.nameInput = 'A New Test Case'

        and: "add a new test step"
        def step = stepService.findAllByProject(project, [max:1]).results.first()
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(step.name)
        page.stepsTable.addBuilderStep(step.name)

        and: "remove row"
        page.stepsTable.removeBuilderRow(1)

        and: "submit form"
        page.scrollToBottom()
        page.createButton.click()

        then: "at show page"
        at ShowBugPage
        def showPage = browser.page(ShowBugPage)
        showPage.getStepsCount() == 1
    }

    void "builder steps are persisted"() {
        given: "get fake data"
        def project = projectService.list(max:1).first()
        def person = personService.list(max:1).first()
        def data = DataFactory.step()
        def step = new Step(project: project, person: person, name: data.name, isBuilderStep: true,
                act: 'here is the action', result: 'this is the result')
        stepService.save(step)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)


        when: "create"
        to(CreateBugPage, project.id)
        CreateBugPage createPage = browser.page(CreateBugPage)
        def bd = DataFactory.bug()
        createPage.createBuilderBug(bd.name, '', '', [], '', step.name, '', '')

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        showPage.isStepsRowDisplayed('here is the action', 'this is the result')
    }

    void "free form steps are persisted"() {
        given: "get fake data"
        def project = projectService.list(max: 1).first()
        def step = new Step(act: 'here is the action', result: 'this is the result')

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)


        when: "create test case"
        to(CreateBugPage, project.id)
        CreateBugPage createPage = browser.page(CreateBugPage)
        def bd = DataFactory.bug()
        createPage.createFreeFormBug(bd.name, bd.description, '', [], '', step.act, step.result, '', '')

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        showPage.isStepsRowDisplayed('here is the action', 'this is the result')
    }
}
