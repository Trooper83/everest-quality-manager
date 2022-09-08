package com.everlution.test.ui.functional.specs.testcase

import com.everlution.PersonService
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Cases')

        then: "correct headers are displayed"
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.listTable.getHeaders() == ["Name", "Description", "Person", "Project", "Platform", "Type"]
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def project = DataFactory.createProject()
        def person = personService.list(max: 1).first()
        def test = new TestCase(person: person, name: "name of test", project: project)
        testCaseService.save(test)

        and: "go to project test cases"
        to(ListTestCasePage, project.id)

        when: "click test name"
        browser.page(ListTestCasePage).listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowTestCasePage
    }

    void "search returns results"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def projectPage = at ProjectHomePage
        projectPage.sideBar.goToProjectDomain('Test Cases')

        when:
        def page = at ListTestCasePage
        page.search('')

        then:
        page.listTable.rowCount > 0
    }

    void "search that returns no results displays message"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def projectPage = at ProjectHomePage
        projectPage.sideBar.goToProjectDomain('Test Cases')

        when:
        def page = at ListTestCasePage
        page.search('adsfasdf')

        then: "at show page"
        page.statusMessage.text() == "No test cases were found using search term: 'adsfasdf'"
    }

    void "delete message displays after test case deleted"() {
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

        when: "delete test case"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.delete()

        then: "at list page and message displayed"
        at ListTestCasePage
        listPage.statusMessage.text() ==~ /TestCase \d+ deleted/
    }

    void "create button not displayed for read only"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        def id = projectService.list().first().id
        go "/project/${id}/testCases"

        then:
        def page = at ListTestCasePage
        !page.createButton.displayed

        where:
        email                           | password
        Credentials.READ_ONLY.email     | Credentials.READ_ONLY.password

    }

    void "create button displayed for basic and above"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        def id = projectService.list().first().id
        go "/project/${id}/testCases"

        then:
        def page = at ListTestCasePage
        page.createButton.displayed

        where:
        email                           | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
    }
}
