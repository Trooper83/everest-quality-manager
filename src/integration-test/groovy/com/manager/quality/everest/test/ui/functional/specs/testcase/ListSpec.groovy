package com.manager.quality.everest.test.ui.functional.specs.testcase

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.ui.support.pages.testcase.ListTestCasePage
import com.manager.quality.everest.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
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
        page.listTable.getHeaders() == ["Name", "Created By", "Area", "Platform", "Type", "Execution Method", "Created", "Updated"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        def page = to(ListTestCasePage, project.id)

        and:
        page.listTable.sortColumn(column)

        expect: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=asc')

        when:
        page.listTable.sortColumn(column)

        then: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=desc')

        where:
        column | propName
        'Name' | 'name'
        'Created By' | 'person'
        'Area' | 'area'
        'Platform' | 'platform'
        'Type' | 'type'
        'Execution Method' | 'executionMethod'
        'Created' | 'dateCreated'
        'Updated' | 'lastUpdated'
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
        page.searchModule.search('test')

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'test'
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

    void "reset button reloads results"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        def page = to(ListTestCasePage, project.id)
        page.searchModule.search('test')

        expect:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'test'

        when:
        page.searchModule.resetSearch()

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == ''
    }

    void "blank value when related items are null"() {
        given:
        def person = personService.list(max: 1).first()
        Project project = projectService.list(max:1).first()
        TestCase testCase = new TestCase(person: person, name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when:
        def page = to ListTestCasePage, project.id

        then:
        page.listTable.getValueInColumn(0, "Area") == ""
        page.listTable.getValueInColumn(0, "Platform") == ""
    }

    void "name displays for related items"() {
        given:
        def a = DataFactory.area()
        def p = DataFactory.area()
        def area = new Area(name: a.name)
        def platform = new Platform(name: p.name)
        def pd = DataFactory.project()
        def person = personService.list(max: 1).first()
        Project proj = new Project(name: pd.name, code: pd.code, areas: [area], platforms: [platform])
        def project = projectService.save(proj)
        TestCase testCase = new TestCase(person: person,name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, area: area, platform: platform)
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to ListTestCasePage, project.id

        then:
        page.listTable.getValueInColumn(0, "Area") == area.name
        page.listTable.getValueInColumn(0, "Platform") == platform.name
    }
}
