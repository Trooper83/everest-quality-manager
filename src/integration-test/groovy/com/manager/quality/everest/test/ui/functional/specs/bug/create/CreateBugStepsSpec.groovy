package com.manager.quality.everest.test.ui.functional.specs.bug.create

import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.StepTemplate
import com.manager.quality.everest.services.steptemplate.StepTemplateService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.ui.support.pages.bug.CreateBugPage
import com.manager.quality.everest.test.ui.support.pages.bug.ShowBugPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreateBugStepsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepTemplateService stepTemplateService

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
        page.stepsTable.addStep("should not persist", "should not persist", "should not persist")

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
        def template = stepTemplateService.findAllInProject(project, [max:1]).results.first()
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(template.name)
        page.stepsTable.addBuilderStep(template.name, false)

        and: "remove row"
        page.stepsTable.appendBuilderSteps()
        page.stepsTable.removeRow(1)

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
        def stepData = DataFactory.step()
        def template = new StepTemplate(name: 'name of the template', act: stepData.action, result: stepData.result,
                person: person, project: project)
        stepTemplateService.save(template)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)


        when: "create"
        to(CreateBugPage, project.id)
        CreateBugPage createPage = browser.page(CreateBugPage)
        def bd = DataFactory.bug()
        createPage.createBuilderBug(bd.name, '', '', [], '', template.name, '', '')

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        showPage.isStepsRowDisplayed(stepData.action, '', stepData.result)
    }

    void "free form steps are persisted"() {
        given: "get fake data"
        def project = projectService.list(max: 1).first()
        def step = DataFactory.step()

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "create test case"
        to(CreateBugPage, project.id)
        CreateBugPage createPage = browser.page(CreateBugPage)
        def bd = DataFactory.bug()
        createPage.createFreeFormBug(bd.name, bd.description, '', [], '', '', step.action, step.data,
                step.result, '', '')

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        showPage.isStepsRowDisplayed(step.action, step.data, step.result)
    }

    void "free form and builder steps are persisted"() {
        given: "get fake data"
        def project = projectService.list(max:1).first()
        def person = personService.list(max:1).first()
        def stepData = DataFactory.step()
        def template = new StepTemplate(name: 'name of the template', act: stepData.action, result: stepData.result,
                person: person, project: project)
        stepTemplateService.save(template)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "create"
        to(CreateBugPage, project.id)
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.nameInput = "Name of the bug"
        createPage.scrollToBottom()
        createPage.stepsTable.addStep("this should be found", stepData.data, "this is a result")
        createPage.stepsTable.addBuilderStep('name of the template')
        createPage.stepsTable.appendBuilderSteps()
        createPage.submit()

        then:
        ShowBugPage showPage = at ShowBugPage
        showPage.isStepsRowDisplayed(stepData.action, "", stepData.result)
        showPage.isStepsRowDisplayed("this should be found", stepData.data, "this is a result")
    }
}
