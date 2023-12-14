package com.everlution.test.ui.functional.specs.step.edit

import com.everlution.StepService
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.step.EditStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditStepSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepService stepService

    @Shared Person person
    @Shared Project project

    def setup() {
        person = personService.list(max: 1).first()
        project = projectService.list(max: 1).first()
    }

    void "authorized users can edit step"(String username, String password) {
        setup: "create step"
        def step = new Step(name: "name", act: "action", result: "result", project: project, person: person, isBuilderStep: true)
        def id = stepService.save(step).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/project/${project.id}/step/edit/${id}"

        when: "edit the step"
        def page = browser.page(EditStepPage)
        page.edit()

        then: "at show page with message displayed"
        at ShowStepPage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all edit form data saved"() {
        setup: "get fake data"
        def step = new Step(name: "name", act: "action", result: "result", project: project, person: person, isBuilderStep: true)
        def id = stepService.save(step).id

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/step/edit/${step.id}"

        and: "edit all bug data"
        def editPage = browser.page(EditStepPage)
        def data = DataFactory.step()
        editPage.editStep(data.action, data.name, data.result)

        then: "data is displayed on show page"
        def showPage = at ShowStepPage
        verifyAll {
            showPage.nameValue.text() == data.name
            showPage.actionValue.text() == data.action
            showPage.resultValue.text() == data.result
        }
    }

    void "links are added to step"() {
        setup: "get fake data"
        Step linked = stepService.findAllByProject(project, [max:1]).results.first()
        def step = new Step(name: "name", act: "action", result: "result", project: project, person: person, isBuilderStep: true)
        def id = stepService.save(step).id

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        to (EditStepPage, project.id, id)

        and: "edit all bug data"
        def editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        editPage.linkModule.addLink(linked.name, 'Is Child of')
        editPage.edit()

        then: "data is displayed on show page"
        def showPage = at ShowStepPage
        showPage.isLinkDisplayed(linked.name, 'parents')
    }

    void "links are removed from step"() {
        given: "get fake data"
        Step linked = stepService.findAllByProject(project, [max:1]).results.first()
        def step = new Step(name: "name", act: "action", result: "result", project: project, person: person, isBuilderStep: true)
        def id = stepService.save(step).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        to (EditStepPage, project.id, id)

        and: "edit all bug data"
        def editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        editPage.linkModule.addLink(linked.name, 'Is Child of')
        editPage.edit()

        expect: "data is displayed on show page"
        def showPage = at ShowStepPage
        showPage.isLinkDisplayed(linked.name, 'parents')

        when:
        showPage.goToEdit()
        def edit = browser.page(EditStepPage)
        edit.scrollToBottom()
        edit.linkModule.removeLinkedItem(0)
        edit.edit()

        then:
        !showPage.isLinkDisplayed(linked.name, 'parents')
    }
}
