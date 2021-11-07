package com.everlution.test.ui.specs.testcase

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ShowPageSpec extends GebSpec {

    ProjectService projectService
    TestCaseService testCaseService

    void "status message displayed after test case created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create test case page"
        to CreateTestCasePage

        when: "create a test case"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.createTestCase()

        then: "at show test case page with message displayed"
        at ShowTestCasePage
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.statusMessage.text() ==~ /TestCase \d+ created/
    }

    void "home link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        when: "click the home button"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        when: "click the list link"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        page.goToList()

        then: "at the list page"
        at ListTestCasePage
    }

    void "create link directs to create view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        when: "click the new test case link"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        page.goToCreate()

        then: "at the create test case page"
        at CreateTestCasePage
    }

    void "edit link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        when: "click the edit button"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        page.goToEdit()

        then: "at the edit test case page"
        at EditTestCasePage
    }

    void "create delete edit buttons not displayed for Read Only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        when: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        then: "create delete edit test case buttons are not displayed"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        verifyAll {
            !page.createLink.displayed
            !page.deleteLink.displayed
            !page.editLink.displayed
        }
    }

    void "create delete edit buttons displayed for authorized users"(String username, String password) {
        given: "login as basic and above user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to list test case page"
        to ListTestCasePage

        when: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        then: "create delete edit test case buttons are not displayed"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        verifyAll {
            page.createLink.displayed
            page.deleteLink.displayed
            page.editLink.displayed
        }

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "correct fields are displayed"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        when: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        then: "correct fields are displayed"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        page.getFields() == ["Creator", "Project", "Area", "Name", "Description", "Execution Method", "Type"]
    }

    void "test case not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        when: "click delete and cancel | verify message"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show test case page"
        at ShowTestCasePage
    }

    void "updated message displays after updating test case"() {
        given: "create test case"
        Project project = projectService.list(max: 10).first()
        TestCase testCase = new TestCase(creator: "test",name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testCase/edit/${id}"

        when: "edit a test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.editTestCase()

        then: "at show test case page with message displayed"
        at ShowTestCasePage
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.statusMessage.text() == "TestCase ${id} updated"
    }
}
