package com.everlution.test.ui.specs.testcase

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class DeleteTestCaseSpec extends GebSpec {

    ProjectService projectService
    TestCaseService testCaseService

    int id

    def setup() {
        def project = projectService.list(max: 1).first()
        def testCase = new TestCase(name: "test case name", creator: "I am the creator", description: "desc", type: "UI",
                executionMethod: "Manual", project: project)
        id = testCaseService.save(testCase).id
    }

    void "authorized users can delete Test Case"(String username, String password) {
        given: "log in as authorized user"
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/testCase/show/${id}"

        when: "delete test case"
        def showPage = browser.at(ShowTestCasePage)
        showPage.deleteTestCase()

        then: "at list page"
        at ListTestCasePage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }
}
