package com.everlution.test.ui.functional.specs.testcase.create

import com.everlution.ProjectService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageFreeFormStepsSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/testCase/create"
    }

    void "add test step row"() {
        given:
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.scrollToBottom()
        page.testStepTable.selectStepsTab('free-form')

        expect: "row count is 0"
        page.testStepTable.getStepsCount() == 0

        when: "add a test step row"
        page.testStepTable.addRow()

        then: "row count is 1"
        page.testStepTable.getStepsCount() == 1
    }

    void "remove test step row"() {
        setup: "add a row"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.scrollToBottom()
        page.testStepTable.selectStepsTab('free-form')
        page.testStepTable.addRow()

        expect: "row count is 1"
        page.testStepTable.getStepsCount() == 1

        when: "remove the first row"
        page.testStepTable.removeRow(0)

        then: "row count is 0"
        page.testStepTable.getStepsCount() == 0
    }

    void "null action and result message"() {
        setup:
        def createPage = browser.page(CreateTestCasePage)
        createPage.completeCreateForm()
        createPage.scrollToBottom()
        createPage.testStepTable.selectStepsTab('free-form')
        createPage.testStepTable.addStep("", "", "")
        createPage.createButton.click()

        expect:
        createPage.errorsMessage*.text() ==
                ["Property [act] with value [null] does not pass custom validation",
                "Property [result] with value [null] does not pass custom validation"]
    }

    void "remove button only displayed for last step"() {
        given: "add a row"
        def page = browser.page(CreateTestCasePage)
        page.scrollToBottom()
        page.testStepTable.selectStepsTab('free-form')
        page.testStepTable.addRow()

        when:
        page.testStepTable.addRow()
        waitFor {
            page.testStepTable.stepsCount == 2
        }

        then:
        !page.testStepTable.getStep(0).find('input[value=Remove]').displayed
        page.testStepTable.getStep(1).find('input[value=Remove]').displayed
    }

    void "alt+n hotkey adds new row with action in focus"() {
        given:
        def page = browser.page(CreateTestCasePage)
        page.scrollToBottom()
        page.testStepTable.selectStepsTab('free-form')

        expect:
        page.testStepTable.getStepsCount() == 0

        when:
        page.testStepTable.addRowHotKey()

        then:
        page.testStepTable.getStepsCount() == 1
        page.testStepTable.getStep(0).find('textarea[name="steps[0].act"]').focused
    }

    void "changing step type resets free form"() {
        setup: "add a row"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.scrollToBottom()
        page.testStepTable.selectStepsTab('free-form')
        page.testStepTable.addRow()

        expect: "row count is 1"
        page.testStepTable.getStepsCount() == 1

        when:
        page.testStepTable.selectStepsTab('builder')
        page.testStepTable.selectStepsTab('free-form')

        then: "row count is 0"
        page.testStepTable.getStepsCount() == 0
    }
}
