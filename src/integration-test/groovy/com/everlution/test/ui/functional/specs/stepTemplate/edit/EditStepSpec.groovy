package com.everlution.test.ui.functional.specs.stepTemplate.edit

import com.everlution.domains.Person
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.domains.StepTemplate
import com.everlution.services.steptemplate.StepTemplateService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.stepTemplate.EditStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ShowStepTemplatePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditStepSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepTemplateService stepTemplateService

    @Shared Person person
    @Shared Project project

    def setup() {
        person = personService.list(max: 1).first()
        project = projectService.list(max: 1).first()
    }

    void "authorized users can edit step"(String username, String password) {
        setup: "create step"
        def step = new StepTemplate(name: "name", act: "action", result: "result", project: project, person: person)
        def id = stepTemplateService.save(step).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/project/${project.id}/stepTemplate/edit/${id}"

        when: "edit the step"
        def page = browser.page(EditStepTemplatePage)
        page.edit()

        then: "at show page with message displayed"
        at ShowStepTemplatePage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all edit form data saved"() {
        setup: "get fake data"
        def step = new StepTemplate(name: "name", act: "action", result: "result", project: project, person: person)
        def id = stepTemplateService.save(step).id

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/stepTemplate/edit/${id}"

        and: "edit all bug data"
        def editPage = browser.page(EditStepTemplatePage)
        def data = DataFactory.step()
        editPage.editStepTemplate(data.action, data.name, data.result)

        then: "data is displayed on show page"
        def showPage = at ShowStepTemplatePage
        verifyAll {
            showPage.nameValue.text() == data.name
            showPage.actionValue.text() == data.action
            showPage.resultValue.text() == data.result
        }
    }

    void "links are added to step"() {
        setup: "get fake data"
        def linked = stepTemplateService.findAllInProject(project, [max:1]).results.first()
        def step = new StepTemplate(name: "name", act: "action", result: "result", project: project, person: person)
        def id = stepTemplateService.save(step).id

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        to (EditStepTemplatePage, project.id, id)

        and: "edit all bug data"
        def editPage = browser.page(EditStepTemplatePage)
        editPage.scrollToBottom()
        editPage.linkModule.addLink(linked.name, 'Is Child of')
        editPage.edit()

        then: "data is displayed on show page"
        def showPage = at ShowStepTemplatePage
        showPage.isLinkDisplayed(linked.name, 'parents')
    }

    void "links are removed from step"() {
        given: "get fake data"
        def linked = stepTemplateService.findAllInProject(project, [max:1]).results.first()
        def step = new StepTemplate(name: "name", act: "action", result: "result", project: project, person: person)
        def id = stepTemplateService.save(step).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        to (EditStepTemplatePage, project.id, id)

        and: "edit all bug data"
        def editPage = browser.page(EditStepTemplatePage)
        editPage.scrollToBottom()
        editPage.linkModule.addLink(linked.name, 'Is Child of')
        editPage.edit()

        expect: "data is displayed on show page"
        def showPage = at ShowStepTemplatePage
        showPage.isLinkDisplayed(linked.name, 'parents')

        when:
        showPage.goToEdit()
        def edit = browser.page(EditStepTemplatePage)
        edit.scrollToBottom()
        edit.linkModule.removeLinkedItem(0)
        edit.edit()

        then:
        !showPage.isLinkDisplayed(linked.name, 'parents')
    }
}
