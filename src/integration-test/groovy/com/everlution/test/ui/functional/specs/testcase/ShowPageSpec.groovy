package com.everlution.test.ui.functional.specs.testcase

import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestCycleService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ShowPageSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService
    TestCycleService testCycleService

    void "status message displayed after test case created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create test case page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/testCase/create"

        when: "create a test case"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.createFreeFormTestCase()

        then: "at show test case page with message displayed"
        at ShowTestCasePage
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.statusMessage.text() ==~ /TestCase \d+ created/
    }

    void "edit link directs to edit view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Cases')

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.listTable.clickCell("Name", 0)

        when: "click the edit button"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        page.goToEdit()

        then: "at the edit test case page"
        at EditTestCasePage
    }

    void "delete edit buttons not displayed for Read Only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Cases')

        when: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.listTable.clickCell("Name", 0)

        then: "delete edit test case buttons are not displayed"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        verifyAll {
            !page.deleteLink.displayed
            !page.editLink.displayed
        }
    }

    void "delete edit buttons displayed for authorized users"(String username, String password) {
        given: "login as basic and above user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Cases')

        when: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.listTable.clickCell("Name", 0)

        then: "delete edit test case buttons are not displayed"
        ShowTestCasePage page = browser.page(ShowTestCasePage)
        verifyAll {
            page.deleteLink.displayed
            page.editLink.displayed
        }

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "test case not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Cases')

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.listTable.clickCell("Name", 0)

        when: "click delete and cancel | verify message"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show test case page"
        at ShowTestCasePage
    }

    void "updated message displays after updating test case"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        TestCase testCase = new TestCase(person: person,name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        when: "edit a test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.editTestCase()

        then: "at show test case page with message displayed"
        at ShowTestCasePage
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.statusMessage.text() == "TestCase ${id} updated"
    }

    void "delete test case with test iterations displays failure error"() {
        given:
        def person = personService.list(max: 1).first()
        def cycle = DataFactory.createTestCycle(person)
        Project project = projectService.list(max: 1).first()
        TestCase testCase = new TestCase(person: person,name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id
        testCycleService.addTestIterations(cycle, [testCase])

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        go "/project/${project.id}/testCase/show/${id}"

        when:
        def show = at ShowTestCasePage
        show.delete()

        then:
        show.errorsMessage.text() == "Test Case has associated Test Iterations and cannot be deleted"
    }
}
