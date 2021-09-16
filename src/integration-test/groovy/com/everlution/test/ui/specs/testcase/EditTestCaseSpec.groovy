package com.everlution.test.ui.specs.testcase

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditTestCaseSpec extends GebSpec {

    ProjectService projectService
    TestCaseService testCaseService

    void "authorized users can edit test case"(String username, String password) {
        given: "create test case"
        Project project = projectService.list(max: 10).first()
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
        username        | password
        "basic"         | "password"
        "project_admin" | "password"
        "org_admin"     | "password"
        "app_admin"     | "password"
    }
}
