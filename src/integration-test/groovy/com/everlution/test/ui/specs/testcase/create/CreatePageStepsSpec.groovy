package com.everlution.test.ui.specs.testcase.create

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageStepsSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        to CreateTestCasePage
    }

    void "add test step row"() {
        expect: "row count is 0"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.getRowCount() == 0

        when: "add a test step row"
        page.testStepTable.addRow()

        then: "row count is 1"
        page.testStepTable.getRowCount() == 1
    }

    void "remove test step row"() {
        setup: "add a row"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.addRow()

        expect: "row count is 1"
        page.testStepTable.getRowCount() == 1

        when: "remove the first row"
        page.testStepTable.removeRow(0)

        then: "row count is 1"
        page.testStepTable.getRowCount() == 0
    }

    void "null action and result message"() {
        setup:
        def createPage = to CreateTestCasePage
        createPage.completeCreateForm()
        createPage.testStepTable.addStep("", "")
        createPage.createButton.click()

        expect:
        createPage.errorsMessage.text() ==
                "Property [action] of class [class com.everlution.Step] with value [null] does not pass custom validation\nProperty [result] of class [class com.everlution.Step] with value [null] does not pass custom validation"
    }
}
