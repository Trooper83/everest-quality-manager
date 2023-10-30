package com.everlution.test.ui.functional.specs.testgroup

import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.EditTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ShowPageSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService
    TestGroupService testGroupService

    void "create message displays after group created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to create"
        def projectId = projectService.list(max: 1).first().id
        go "project/${projectId}/testGroup/create"

        when: "create instance"
        def createPage = browser.page(CreateTestGroupPage)
        createPage.createTestGroup()

        then: "at show page and message displayed"
        def showPage = at ShowTestGroupPage
        showPage.statusMessage.text() ==~ /TestGroup \d+ created/
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
        projectHomePage.sideBar.goToProjectDomain('Test Groups')

        and: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

        when: "click the edit button"
        ShowTestGroupPage page = browser.page(ShowTestGroupPage)
        page.goToEdit()

        then: "at the edit page"
        at EditTestGroupPage
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
        projectHomePage.sideBar.goToProjectDomain('Test Groups')

        when: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

        then: "create delete edit buttons are not displayed"
        def page = browser.page(ShowTestGroupPage)
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
        projectHomePage.sideBar.goToProjectDomain('Test Groups')

        when: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

        then: "create delete edit buttons are not displayed"
        def page = browser.page(ShowTestGroupPage)
        verifyAll {
            page.deleteLink.displayed
            page.editLink.displayed
        }

        where:
        username                         | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "test group not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Groups')

        and: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

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
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Groups')

        and: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

        and:
        ShowTestGroupPage showPage = at ShowTestGroupPage
        showPage.goToEdit()

        when: "edit instance"
        def page = browser.page(EditTestGroupPage)
        page.edit()

        then: "at show page with message displayed"
        showPage.statusMessage.text() ==~ /TestGroup \d+ updated/
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
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to show page"
        to (ShowTestGroupPage, project.id, group.id)

        then: "at show page"

        ShowTestGroupPage showPage = at ShowTestGroupPage
        showPage.testCaseTable.getHeaders() == ["Name", "Area", "Platform", "Type", "Execution Method"]
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
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to show page"
        to (ShowTestGroupPage, project.id, group.id)

        when: "at show page"
        ShowTestGroupPage showPage = at ShowTestGroupPage
        showPage.testCaseTable.clickCell("Name", 0)

        then: "at show test case page"
        at ShowTestCasePage
    }

    void "pagination works for results"() {
        given: "login as a basic user"
        def person = personService.list(max:1).first()
        def data = DataFactory.testGroup()
        TestGroup group = new TestGroup(name: data.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)

        for (int i = 0; i <= 12; i++) {
            def td = DataFactory.testCase()
            def testCase = new TestCase(name: td.name, project: project, person: person, testGroups: [group])
            testCaseService.save(testCase)
        }

        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to (ShowTestGroupPage, project.id, group.id)
        def found = page.testCaseTable.getValueInColumn(0, 'Name')
        page.scrollToBottom()
        page.testCaseTable.goToPage('2')

        then:
        at ShowTestGroupPage
        !page.testCaseTable.isValueInColumn('Name', found)
    }
}
