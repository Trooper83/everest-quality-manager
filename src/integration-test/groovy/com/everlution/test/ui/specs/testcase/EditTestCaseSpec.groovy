package com.everlution.test.ui.specs.testcase

import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestStep
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditTestCaseSpec extends GebSpec {

    TestCaseService testCaseService

    void "authorized users can edit test case"(String username, String password) {
        given: "create test case"
        TestStep testStep = new TestStep(action: "do something", result: "something happened")
        TestCase testCase = new TestCase(creator: "test", name: "user can edit", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep])
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        when: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.editTestCase()

        then: "at show test case page with message displayed"
        at ShowTestCasePage

        where:
        username        | password
        "basic"         | "password"
        "project_admin" | "password"
        "org_admin"     | "password"
        "app_admin"     | "password"
    }
}
