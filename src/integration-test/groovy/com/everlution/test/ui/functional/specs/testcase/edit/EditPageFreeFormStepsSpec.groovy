package com.everlution.test.ui.functional.specs.testcase.edit

import com.everlution.domains.Person
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.domains.Step
import com.everlution.domains.TestCase
import com.everlution.services.testcase.TestCaseService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class EditPageFreeFormStepsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "add test step row"() {
        given: "test case"
        Project project = projectService.list(max: 1).first()
        Step testStep = new Step(act: "step1", result: "result1")
        TestCase testCase = new TestCase(person: person, name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        expect: "row count is 1"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.getStepsCount() == 1

        when: "add a test step row"
        page.stepsTable.addRow()

        then: "row count is 2"
        page.stepsTable.getStepsCount() == 2
    }

    void "remove test step row"() {
        given: "test case"
        Project project = projectService.list(max: 1).first()
        Step testStep = new Step(act: "step123", result: "result123")
        TestCase testCase = new TestCase(person: person, name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        and: "add a row"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addRow()

        expect: "row count is 2"
        page.stepsTable.getStepsCount() == 2

        when: "remove the second row"
        page.stepsTable.removeRow(1)

        then: "row count is 1"
        page.stepsTable.getStepsCount() == 1
    }

    void "removing step adds hidden input"() {
        given: "test case"
        Project project = projectService.list(max: 1).first()
        Step testStep = new Step(act: "step123", result: "result123")
        TestCase testCase = new TestCase(person: person, name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        expect:
        testCase.steps.size() == 1

        when: "remove step"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.removeRow(0)

        then: "hidden input added"
        page.stepRemovedInput.size() == 1
    }

    void "remove button only displayed for last step"() {
        given: "test case"
        Project project = projectService.list(max: 1).first()
        TestCase testCase = new TestCase(person: person, name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, steps: [])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        and:
        def page = browser.page(EditTestCasePage)
        page.scrollToBottom()

        when:
        page.stepsTable.addRow()
        page.stepsTable.addRow()
        waitFor {
            page.stepsTable.stepsCount > 0
        }

        then:
        !page.stepsTable.getStep(0).find('input[value=Remove]').displayed
        page.stepsTable.getStep(1).find('input[value=Remove]').displayed
    }
}
