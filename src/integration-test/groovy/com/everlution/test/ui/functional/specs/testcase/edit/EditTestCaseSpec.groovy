package com.everlution.test.ui.functional.specs.testcase.edit

import com.everlution.Area
import com.everlution.Environment
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.StepTemplate
import com.everlution.StepTemplateService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestGroup
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditTestCaseSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepTemplateService stepTemplateService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "authorized users can edit test case"(String username, String password) {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        TestCase testCase = new TestCase(person: person, name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        when: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.editTestCase()

        then: "at show test case page with message displayed"
        at ShowTestCasePage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "free-form step can be added to existing test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        TestCase testCase = new TestCase(person: person, name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        when: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.selectStepsTab('free-form')
        page.stepsTable.addStep("added action", "added data", "added result")
        page.editTestCase()

        then: "at show view with added step"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.isStepsRowDisplayed("added action",  "added data","added result")
    }

    void "free-form step can be edited on existing test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        TestCase testCase = new TestCase(person: person, name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project,
                steps: [new Step(act: "changelog entry", result: "changelog entry", data: "changelog entry")])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        when: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.stepsTable.editTestStep(0, "edited action", "edited data","edited result")
        page.editTestCase()

        then: "at show view with edited step values"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.isStepsRowDisplayed("edited action", "edited data", "edited result")
    }

    void "free-form step can be deleted from existing test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        def step = new Step(act: "action", result: "result")
        TestCase testCase = new TestCase(person: person, name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project,
                steps: [step])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        expect:
        step.id != null
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.getStepsCount() == 1

        when: "edit the test case"
        page.stepsTable.removeRow(0)
        page.editTestCase()

        then: "at show view with edited step values"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.getStepsCount() == 0
        !showPage.isStepsRowDisplayed("action", "","result")
    }

    void "builder step can be added to existing test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        def step = stepTemplateService.findAllInProject(project, [max: 1]).results.first()
        TestCase testCase = new TestCase(person: person, name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        when: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(step.name)
        page.editTestCase()

        then: "at show view with added step"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.isStepsRowDisplayed(step.act, '', step.result)
    }

    void "builder step can be removed from existing test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        def template = new StepTemplate(act: "action", result: "result", project: project, person: person, name: 'this is a test step')
        stepTemplateService.save(template)
        def step = new Step(act: "action", result: "result", template: template, isBuilderStep: true)
        TestCase testCase = new TestCase(person: person, name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project,
                steps: [step])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        expect:
        step.id != null
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.getBuilderStepsCount() == 1

        when: "edit the test case"
        page.stepsTable.removeBuilderRow(0)
        page.editTestCase()

        then: "at show view with edited step values"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.getStepsCount() == 0
        !showPage.isStepsRowDisplayed("action", "", "result")
    }

    void "all edit from data saved"() {
        setup: "get fake data"
        def area = new Area(DataFactory.area())
        def env = new Environment(DataFactory.environment())
        def group = new TestGroup(name: "test group 1")
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code,
                areas: [area], environments: [env], testGroups: [group]))
        def td = DataFactory.testCase()
        def testCase = new TestCase(name: td.name, description: td.description, person: person, project: project,
            area: area, executionMethod: "Manual", type: "API", platform: "Web", environments: [env],
            testGroups: [group])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        when: "edit test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        def edited = DataFactory.testCase()
        page.editTestCase(edited.name, edited.description, "", [""], "Automated", "UI", "iOS",
                [""])

        then: "data is displayed on show page"
        ShowTestCasePage showPage = at ShowTestCasePage
        verifyAll {
            showPage.areaValue.text() == ""
            !showPage.areEnvironmentsDisplayed([env.name])
            !showPage.areTestGroupsDisplayed([group.name])
            showPage.nameValue.text() == edited.name
            showPage.descriptionValue.text() == edited.description
            showPage.executionMethodValue.text() == "Automated"
            showPage.typeValue.text() == "UI"
            showPage.platformValue.text() == "iOS"
        }
    }
}
