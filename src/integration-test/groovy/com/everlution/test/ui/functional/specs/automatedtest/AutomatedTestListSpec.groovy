package com.everlution.test.ui.functional.specs.automatedtest

import com.everlution.AutomatedTest
import com.everlution.AutomatedTestService
import com.everlution.ProjectService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.automatedtest.ListAutomatedTestPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class AutomatedTestListSpec extends GebSpec {

    AutomatedTestService automatedTestService
    ProjectService projectService

    private Long setupData() {
        def project = projectService.list(max:1).first()

        if (automatedTestService.findAllByProject(project, [:]).count <= 25) {
            for (int i = 0; i <= 26; i++) {
                def t = new AutomatedTest(fullName: "Pagination ATest ${i}", project: project)
                automatedTestService.save(t)
            }
        }
        return project.id
    }

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and:
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Automated Tests')

        when: "go to list page"
        def page = at ListAutomatedTestPage

        then: "correct headers are displayed"
        page.listTable.getHeaders() == ["Full Name", "Name", "Created"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projId = projectService.list(max:1).first().id
        def page = to(ListAutomatedTestPage, projId)

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
        column       | propName
        'Full Name'  | 'fullName'
        'Name'       | 'name'
        'Created'    | 'dateCreated'
    }

    void "search text field retains search value"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def page = at ProjectHomePage
        page.sideBar.goToProjectDomain('Automated Tests')

        when:
        def list = at ListAutomatedTestPage
        list.searchModule.search('auto test')

        then:
        list.searchModule.searchInput.text == 'auto test'
    }

    void "search lists automated tests"() {
        given: "login as a project admin user"
        def p = projectService.list(max:1).first()
        automatedTestService.save(new AutomatedTest(project: p, fullName: "auto test search"))

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when:
        def list = to(ListAutomatedTestPage, p.id)
        list.searchModule.search('auto test')

        then:
        list.listTable.rowCount > 0
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        def p = projectService.list(max:1).first()
        automatedTestService.save(new AutomatedTest(project: p, fullName: "auto test direction"))

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        def listPage = to(ListAutomatedTestPage, project.id)

        when: "click first scenario in list"
        listPage.listTable.clickCell("Full Name", 0)

        then: "at show page"
        at ShowAutomatedTestPage
    }

    void "pagination works for results"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListAutomatedTestPage, id)

        when:
        def found = page.listTable.getValueInColumn(0, 'Full Name')
        page.scrollToBottom()
        page.listTable.goToPage('2')

        then:
        at ListAutomatedTestPage
        !page.listTable.isValueInColumn('Full Name', found)
    }

    void "pagination params remain set with sorting"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListAutomatedTestPage, id)

        and:
        page.scrollToBottom()
        page.listTable.goToPage('2')

        when:
        page.listTable.sortColumn('Full Name')

        then:
        currentUrl.contains('max=25')
        currentUrl.contains('offset=25')
    }

    void "sort params remain set with pagination"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def page = to(ListAutomatedTestPage, id)
        page.listTable.sortColumn('Full Name')

        when:
        page.scrollToBottom()
        page.listTable.goToPage('2')

        then:
        currentUrl.contains('sort=fullName')
        currentUrl.contains('order=asc')
    }

    void "search params remain set with pagination"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def page = to(ListAutomatedTestPage, id)
        page.searchModule.search('pagination')

        when:
        page.scrollToBottom()
        page.listTable.goToPage('2')

        then:
        currentUrl.contains('searchTerm=pagination')
        currentUrl.contains('isSearch=true')
    }

    void "search params remain set with sort"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def page = to(ListAutomatedTestPage, id)
        page.searchModule.search('pagination')

        when:
        page.listTable.sortColumn('Full Name')

        then:
        currentUrl.contains("searchTerm=pagination")
        currentUrl.contains('isSearch=true')
    }

    void "reset button reloads results"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def page = to(ListAutomatedTestPage, id)
        page.searchModule.search('pagination')

        expect:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'pagination'

        when:
        page.searchModule.resetSearch()

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == ''
    }
}
