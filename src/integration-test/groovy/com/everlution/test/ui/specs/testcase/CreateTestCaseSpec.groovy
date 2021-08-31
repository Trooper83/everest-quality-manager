package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import org.springframework.test.annotation.Rollback

@Rollback
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

        then: "at show test case page with message displayed"
        at ShowTestCasePage

        where:
        username        | password
        "basic"         | "password"
        "project_admin" | "password"
        "org_admin"     | "password"
        "app_admin"     | "password"
    }

    void "removed test steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to the create test case page"
        to CreateTestCasePage

        when: "fill in create form"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.completeCreateForm()

        and: "add a new test step"
        page.testStepTable.addTestStep("should not persist", "should not persist", false)

        and: "remove row"
        page.testStepTable.removeRow(1)

        and: "submit form"
        page.createButton.click()

        then: "at show test case page"
        at ShowTestCasePage
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.testStepTable.getRowCount() == 1
    }
}
