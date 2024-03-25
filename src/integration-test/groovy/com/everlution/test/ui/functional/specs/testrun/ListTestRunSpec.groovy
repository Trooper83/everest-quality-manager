package com.everlution.test.ui.functional.specs.testrun

import com.everlution.services.project.ProjectService
import com.everlution.domains.TestRun
import com.everlution.services.testrun.TestRunService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testrun.ListTestRunPage
import com.everlution.test.ui.support.pages.testrun.ShowTestRunPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListTestRunSpec extends GebSpec {

    ProjectService projectService
    TestRunService testRunService

    private Long setupData() {
        def project = projectService.list(max:1).first()

        if (testRunService.findAllByProject(project, [:]).count <= 25) {
            for (int i = 0; i <= 26; i++) {
                def t = new TestRun(name: "Pagination ATest ${i}", project: project)
                testRunService.save(t)
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
        projectHomePage.sideBar.goToProjectDomain('Test Runs')

        when: "go to list page"
        def page = at ListTestRunPage

        then: "correct headers are displayed"
        page.listTable.getHeaders() == ["Name", "Created"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projId = projectService.list(max:1).first().id
        def page = to(ListTestRunPage, projId)

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
        page.sideBar.goToProjectDomain('Test Runs')

        when:
        def list = at ListTestRunPage
        list.searchModule.search('auto test run')

        then:
        list.searchModule.searchInput.text == 'auto test run'
    }

    void "search lists automated tests"() {
        given: "login as a project admin user"
        def p = projectService.list(max:1).first()
        testRunService.save(new TestRun(project: p, name: "auto test run search"))

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when:
        def list = to(ListTestRunPage, p.id)
        list.searchModule.search('auto test run')

        then:
        list.listTable.rowCount > 0
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        def p = projectService.list(max:1).first()
        testRunService.save(new TestRun(project: p, name: "auto test run direction"))

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        def listPage = to(ListTestRunPage, project.id)

        when: "click first scenario in list"
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowTestRunPage
    }

    void "pagination works for results"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListTestRunPage, id)

        when:
        def found = page.listTable.getValueInColumn(0, 'Name')
        page.scrollToBottom()
        page.listTable.goToPage('2')

        then:
        at ListTestRunPage
        !page.listTable.isValueInColumn('Name', found)
    }

    void "pagination params remain set with sorting"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListTestRunPage, id)

        and:
        page.scrollToBottom()
        page.listTable.goToPage('2')

        when:
        page.listTable.sortColumn('Name')

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
        def page = to(ListTestRunPage, id)
        page.listTable.sortColumn('Name')

        when:
        page.scrollToBottom()
        page.listTable.goToPage('2')

        then:
        currentUrl.contains('sort=name')
        currentUrl.contains('order=asc')
    }

    void "search params remain set with pagination"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def page = to(ListTestRunPage, id)
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
        def page = to(ListTestRunPage, id)
        page.searchModule.search('pagination')

        when:
        page.listTable.sortColumn('Name')

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
        def page = to(ListTestRunPage, id)
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
