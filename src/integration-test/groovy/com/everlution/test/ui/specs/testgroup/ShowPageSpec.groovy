package com.everlution.test.ui.specs.testgroup

import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.EditTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class ShowPageSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService
    TestGroupService testGroupService

    @Shared Long id

    def setup() {
        id = testGroupService.list(max: 1).first().id
    }

    void "create message displays after group created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to create"
        go "/testGroup/create"

        when: "create instance"
        def createPage = browser.page(CreateTestGroupPage)
        createPage.createTestGroup()

        then: "at show page and message displayed"
        def showPage = at ShowTestGroupPage
        showPage.statusMessage.text() ==~ /TestGroup \d+ created/
    }

    void "home link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        go "/testGroup/show/${id}"

        when: "click the home button"
        ShowTestGroupPage page = browser.page(ShowTestGroupPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        go "/testGroup/show/${id}"

        when: "click the list link"
        def page = browser.page(ShowTestGroupPage)
        page.goToList()

        then: "at the list page"
        at ListTestGroupPage
    }

    void "create link directs to create view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        go "/testGroup/show/${id}"

        when: "click the create link"
        ShowTestGroupPage page = browser.page(ShowTestGroupPage)
        page.goToCreate()

        then: "at the create page"
        at CreateTestGroupPage
    }

    void "edit link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        go "/testGroup/show/${id}"

        when: "click the edit button"
        ShowTestGroupPage page = browser.page(ShowTestGroupPage)
        page.goToEdit()

        then: "at the edit page"
        at EditTestGroupPage
    }

    void "create delete edit buttons not displayed for Read Only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to show page"
        go "/testGroup/show/${id}"

        then: "create delete edit buttons are not displayed"
        def page = browser.page(ShowTestGroupPage)
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

        when: "go to show page"
        go "/testGroup/show/${id}"

        then: "create delete edit buttons are not displayed"
        def page = browser.page(ShowTestGroupPage)
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

        when: "go to show page"
        go "/testGroup/show/${id}"

        then: "correct fields are displayed"
        def page = browser.page(ShowTestGroupPage)
        page.getFields() == ["Project", "Name"]
    }

    void "test group not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        go "/testGroup/show/${id}"

        when: "click delete and cancel | verify message"
        def showPage = browser.page(ShowTestGroupPage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show page"
        at ShowTestGroupPage
    }

    void "updated message displays after updating test group"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testGroup/edit/${id}"

        when: "edit instance"
        def page = browser.page(EditTestGroupPage)
        page.edit()

        then: "at show page with message displayed"
        ShowTestGroupPage showPage = at ShowTestGroupPage
        showPage.statusMessage.text() == "TestGroup ${id} updated"
    }

    void "verify table headers order"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to show page"
        go "/testGroup/show/${group.id}"

        then: "at show page"
        ShowTestGroupPage showPage = at ShowTestGroupPage
        showPage.testCaseTable.getHeaders() == ["Id", "Name", "Area", "Platform", "Environments", "Type", "Execution Method"]
    }

    void "test case table id link opens show test case"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to show page"
        go "/testGroup/show/${id}"

        when: "at show page"
        ShowTestGroupPage showPage = at ShowTestGroupPage
        showPage.testCaseTable.clickCell("Id", 0)

        then: "at show test case page"
        at ShowTestCasePage
    }
}
