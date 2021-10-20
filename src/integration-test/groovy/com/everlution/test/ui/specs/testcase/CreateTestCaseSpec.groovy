package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateTestCaseSpec extends GebSpec {

    void "authorized users can create test case"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create test case page"
        to CreateTestCasePage

        when: "create a test case"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.createTestCase()

        then: "at show test case page"
        at ShowTestCasePage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "removed test steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        def page = to CreateTestCasePage

        when: "fill in create form"
        page.completeCreateForm()

        and: "add a new test step"
        page.testStepTable.addStep("should not persist", "should not persist")

        and: "remove row"
        page.testStepTable.removeRow(1)

        and: "submit form"
        page.createButton.click()

        then: "at show page"
        at ShowTestCasePage
        def showPage = browser.page(ShowTestCasePage)
        showPage.testStepTable.getRowCount() == 1
    }
}
