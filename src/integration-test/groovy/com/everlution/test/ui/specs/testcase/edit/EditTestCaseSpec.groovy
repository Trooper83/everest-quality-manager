package com.everlution.test.ui.specs.testcase.edit

import com.everlution.Area
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestStepService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditTestCaseSpec extends GebSpec {

    ProjectService projectService
    TestCaseService testCaseService
    TestStepService testStepService

    void "authorized users can edit test case"(String username, String password) {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        TestCase testCase = new TestCase(creator: "testing", name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/testCase/edit/${id}"

        when: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.editTestCase()

        then: "at show test case page with message displayed"
        at ShowTestCasePage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "step can be added to existing test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        TestCase testCase = new TestCase(creator: "testing", name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        when: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.testStepTable.addStep("added action", "added result")
        page.editTestCase()

        then: "at show view with added step"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.testStepTable.isRowDisplayed("added action", "added result")
    }

    void "step can be edited on existing test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        TestCase testCase = new TestCase(creator: "testing", name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project,
                steps: [new Step(action: "initial entry", result: "initial entry")])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        when: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.testStepTable.editTestStep(0, "edited action", "edited result")
        page.editTestCase()

        then: "at show view with edited step values"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.testStepTable.isRowDisplayed("edited action", "edited result")
    }

    void "step can be deleted from existing test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        def step = new Step(action: "action", result: "result")
        TestCase testCase = new TestCase(creator: "testing", name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project,
                steps: [step])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        expect:
        step.id != null
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.testStepTable.getRowCount() == 1

        when: "edit the test case"
        page.testStepTable.removeRow(0)
        page.editTestCase()

        then: "at show view with edited step values"
        ShowTestCasePage showPage = at ShowTestCasePage
        showPage.testStepTable.getRowCount() == 0
        !showPage.testStepTable.isRowDisplayed("action", "result")
        testStepService.get(step.id) == null
    }

    void "all edit from data saved"() {
        setup: "get fake data"
        def area = new Area(DataFactory.area())
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code, areas: [area]))
        def td = DataFactory.testCase()
        def testCase = new TestCase(name: td.name, description: td.description, creator: td.creator, project: project,
            area: area, executionMethod: "Manual", type: "API")
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        when: "edit test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        def edited = DataFactory.testCase()
        page.editTestCase(edited.name, edited.description, "", "Automated", "UI")

        then: "data is displayed on show page"
        ShowTestCasePage showPage = at ShowTestCasePage
        verifyAll {
            showPage.areaValue.text() == ""
            showPage.projectValue.text() == project.name
            showPage.nameValue.text() == edited.name
            showPage.descriptionValue.text() == edited.description
            showPage.executionMethodValue.text() == "Automated"
            showPage.typeValue.text() == "UI"
        }
    }
}
