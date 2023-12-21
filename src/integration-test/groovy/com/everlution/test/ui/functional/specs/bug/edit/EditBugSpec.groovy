package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.Area
import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Environment
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.StepService
import com.everlution.StepTemplate
import com.everlution.StepTemplateService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditBugSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService
    StepService stepService
    StepTemplateService stepTemplateService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "authorized users can edit bug"(String username, String password) {
        setup: "create bug"
        def project = projectService.list(max: 10).first()
        def bug = new Bug(person: person, name: "name of bug", project: project, status: "Open", actual: "actual",
                expected: "expected")
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        when: "edit the bug"
        def page = browser.page(EditBugPage)
        page.edit()

        then: "at show page with message displayed"
        at ShowBugPage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all edit from data saved"() {
        setup: "get fake data"
        def area = new Area(DataFactory.area())
        def env = new Environment(DataFactory.environment())
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code,
                areas: [area], environments: [env]))
        def bugData = DataFactory.bug()
        def bug = bugService.save(new Bug(person: person, name: bugData.name,
                description: bugData.description, project: project, area: area, platform: "Web", status: "Open",
                actual: "actual", expected: "expected"))

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${bug.id}"

        and: "edit all bug data"
        EditBugPage editPage = browser.page(EditBugPage)
        def data = DataFactory.bug()
        editPage.editBug(data.name, data.description, "",[""], "", data.expected, data.actual)

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        verifyAll {
            showPage.areaValue.text() == ""
            showPage.nameValue.text() == data.name
            showPage.actualValue.text() == data.actual
            showPage.expectedValue.text() == data.expected
            showPage.platformValue.text() == ""
            showPage.statusValue.text() == "Closed"
            showPage.descriptionValue.text() == data.description
            !showPage.areEnvironmentsDisplayed([env.name])
        }
    }

    void "free-form step can be added to existing bug"() {
        given: "create bug"
        Project project = projectService.list(max: 1).first()
        def bug = new Bug(person: person, name: "first1", project: project, status: 'Open')
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        when: "edit the bug"
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.selectStepsTab('free-form')
        page.stepsTable.addStep("added action", "added data", "added result")
        page.edit()

        then: "at show view with added step"
        ShowBugPage showPage = at ShowBugPage
        showPage.isStepsRowDisplayed("added action", "added data", "added result")
    }

    void "free-form step can be edited on existing bug"() {
        given: "create bug"
        Project project = projectService.list(max: 1).first()
        Bug bug = new Bug(person: person, name: "first1", project: project, status: 'Open',
                steps: [new Step(act: "changelog entry", data: "changelog data", result: "changelog entry")])
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        when: "edit the bug"
        EditBugPage page = browser.page(EditBugPage)
        page.stepsTable.editTestStep(0, "edited action", "edited data", "edited result")
        page.edit()

        then: "at show view with edited step values"
        ShowBugPage showPage = at ShowBugPage
        showPage.isStepsRowDisplayed("edited action", "edited data", "edited result")
    }

    void "free-form step can be deleted from existing bug"() {
        given: "create bug"
        Project project = projectService.list(max: 1).first()
        def step = new Step(act: "action", result: "result", data: "data")
        Bug bug = new Bug(person: person, name: "first1", project: project, steps: [step], status: 'Open')
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        expect:
        step.id != null
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.getStepsCount() == 1

        when: "edit the bug"
        page.stepsTable.removeRow(0)
        page.edit()

        then: "at show view with edited step values"
        ShowBugPage showPage = at ShowBugPage
        showPage.getStepsCount() == 0
        !showPage.isStepsRowDisplayed("action", "data", "result")
    }

    void "builder step can be added to existing bug"() {
        given: "create bug"
        Project project = projectService.list(max: 1).first()
        def template = stepTemplateService.findAllInProject(project, [max: 1]).results.first()
        Bug bug = new Bug(person: person, name: "first1", project: project, status: 'Open')
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        when: "edit the bug"
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(template.name)
        page.edit()

        then: "at show view with added step"
        ShowBugPage showPage = at ShowBugPage
        showPage.isStepsRowDisplayed(template.act, "", template.result)
    }

    void "builder step can be removed from existing bug"() {
        given: "create bug"
        Project project = projectService.list(max: 1).first()
        def template = new StepTemplate(act: "action", result: "result", project: project, person: person,
                name: 'this is a test step')
        stepTemplateService.save(template)
        def step = new Step(act: template.act, result: template.result, template: template, isBuilderStep: true)
        Bug bug = new Bug(person: person, name: "first1", project: project, steps: [step], status: 'Open')
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        expect:
        step.id != null
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.getBuilderStepsCount() == 1

        when: "edit the bug"
        page.stepsTable.removeBuilderRow(0)
        page.edit()

        then: "at show view with edited step values"
        ShowBugPage showPage = at ShowBugPage
        showPage.getStepsCount() == 0
        !showPage.isStepsRowDisplayed("action", "", "result")
    }
}
