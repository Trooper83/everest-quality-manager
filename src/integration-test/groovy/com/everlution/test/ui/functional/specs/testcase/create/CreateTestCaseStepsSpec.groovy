package com.everlution.test.ui.functional.specs.testcase.create

import com.everlution.StepTemplate
import com.everlution.StepTemplateService
import com.everlution.PersonService
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateTestCaseStepsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepTemplateService stepTemplateService

    void "removed test free form steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/testCase/create"

        when: "fill in create form"
        def page = browser.page(CreateTestCasePage)
        page.completeCreateForm()

        and: "add a new test step"
        page.testStepTable.selectStepsTab('free-form')
        page.testStepTable.addStep("should not persist", "", "should not persist")

        and: "remove row"
        page.testStepTable.removeRow(1)

        and: "submit form"
        page.createButton.click()

        then: "at show page"
        at ShowTestCasePage
        def showPage = browser.page(ShowTestCasePage)
        showPage.getStepsCount() == 1
    }

    void "removed test builder steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/testCase/create"

        when: "fill in create form"
        def page = browser.page(CreateTestCasePage)
        page.nameInput = 'A New Test Case'

        and: "add a new test step"
        def step = stepTemplateService.findAllInProject(project, [max:1]).results.first()
        page.scrollToBottom()
        page.testStepTable.addBuilderStep(step.name)
        page.testStepTable.addBuilderStep(step.name)

        and: "remove row"
        page.testStepTable.removeBuilderRow(1)

        and: "submit form"
        page.scrollToBottom()
        page.createButton.click()

        then: "at show page"
        at ShowTestCasePage
        def showPage = browser.page(ShowTestCasePage)
        showPage.getStepsCount() == 1
    }

    void "builder steps are persisted"() {
        given: "get fake data"
        def project = projectService.list(max:1).first()
        def person = personService.list(max:1).first()
        def step = new StepTemplate(project: project, person: person, name: 'what kind of name should I use',
                act: 'here is the action', result: 'this is the result')
        def step1 = new StepTemplate(project: project, person: person, name: '1what kind of name should I use',
                act: '1here is the action', result: '1this is the result')
        stepTemplateService.save(step)
        stepTemplateService.save(step1)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)


        when: "create test case"
        to(CreateTestCasePage, project.id)
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        def tcd = DataFactory.testCase()
        createPage.createBuilderTestCase(
                tcd.name, tcd.description, '', [], [],"Automated", "UI", "Web",
                [step.name, step1.name])

        then: "data is displayed on show page"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.isStepsRowDisplayed('here is the action', '', 'this is the result')
        showPage.isStepsRowDisplayed('1here is the action', '', '1this is the result')
    }

    void "free form steps are persisted"() {
        given: "get fake data"
        def project = projectService.list(max:1).first()
        def step = new Step(act: 'here is the action', result: 'this is the result', data: 'testing')
        def step1 = new Step(act: '1here is the action', result: '1this is the result', data: 'testing')

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)


        when: "create test case"
        to(CreateTestCasePage, project.id)
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        def tcd = DataFactory.testCase()
        createPage.createFreeFormTestCase(
                tcd.name, tcd.description, '', [], [],"Automated", "UI", "Web", [step, step1], tcd.verify)

        then: "data is displayed on show page"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.isStepsRowDisplayed('here is the action', 'testing','this is the result')
        showPage.isStepsRowDisplayed('1here is the action', 'testing','1this is the result')
    }
}
