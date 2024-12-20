package com.manager.quality.everest.test.ui.functional.specs.bug

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.ui.support.pages.bug.ListBugPage
import com.manager.quality.everest.test.ui.support.pages.bug.ShowBugPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ListSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    private Long setupData() {
        def person = personService.list(max:1).first()
        def project = projectService.list(max:1).first()

        if (bugService.findAllByProject(project, [:]).count <= 25) {
            for (int i = 0; i <= 26; i++) {
                def b = new Bug(name: "Pagination Bug ${i}", person: person, project: project, status: "Open")
                bugService.save(b)
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

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        when: "go to list page"
        def page = at ListBugPage

        then: "correct headers are displayed"
        page.listTable.getHeaders() == ["Name", "Created By", "Platform", "Area", "Status", "Created", "Updated"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projId = projectService.list(max:1).first().id
        def page = to(ListBugPage, projId)

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
        'Created By' | 'person'
        'Platform'   | 'platform'
        'Area'       | 'area'
        'Status'     | 'status'
        'Created'    | 'dateCreated'
        'Updated'    | 'lastUpdated'
    }

    void "delete message displays after bug deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        and: "go to list page"
        def page = at ListBugPage
        page.listTable.clickCell('Name', 0)

        when: "delete bug"
        def showPage = browser.page(ShowBugPage)
        showPage.delete()

        then: "at list page and message displayed"
        def listPage = at ListBugPage
        listPage.statusMessage.text() ==~ /Bug \d+ deleted/
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
        page.sideBar.goToProjectDomain('Bugs')

        when:
        def bugsPage = at ListBugPage
        bugsPage.searchModule.search('bug')

        then:
        bugsPage.listTable.rowCount > 0
        bugsPage.searchModule.searchInput.text == 'bug'
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/bugs"

        when: "click first scenario in list"
        def listPage = browser.page(ListBugPage)
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowBugPage
    }

    void "pagination next button not displayed"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListBugPage, id)

        when:
        page.scrollToBottom()
        page.listTable.goToPage("2")

        then:
        !page.listTable.isPaginationButtonDisplayed('Next')
    }

    void "pagination works for results"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListBugPage, id)

        when:
        def found = page.listTable.getValueInColumn(0, 'Name')
        page.scrollToBottom()
        page.listTable.goToPage('2')

        then:
        at ListBugPage
        !page.listTable.isValueInColumn('Name', found)
    }

    void "pagination params remain set with sorting"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListBugPage, id)

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
        def page = to(ListBugPage, id)
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
        def page = to(ListBugPage, id)
        page.searchModule.search('Bug')

        when:
        page.scrollToBottom()
        page.listTable.goToPage('2')

        then:
        currentUrl.contains('searchTerm=Bug')
        currentUrl.contains('isSearch=true')
    }

    void "search params remain set with sort"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def page = to(ListBugPage, id)
        page.searchModule.search('Bug')

        when:
        page.listTable.sortColumn('Name')

        then:
        currentUrl.contains("searchTerm=Bug")
        currentUrl.contains('isSearch=true')
    }

    void "pagination previous button displayed"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListBugPage, id)

        when:
        page.scrollToBottom()
        page.listTable.goToPage('2')

        then:
        page.listTable.isPaginationButtonDisplayed('Previous')
    }

    void "pagination next button displayed"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListBugPage, id)

        when:
        page.scrollToBottom()

        then:
        page.listTable.isPaginationButtonDisplayed('Next')
    }

    void "pagination previous button not displayed"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ListBugPage, id)

        when:
        page.scrollToBottom()

        then:
        !page.listTable.isPaginationButtonDisplayed('Previous')
    }

    void "reset button reloads results"() {
        given:
        def id = setupData()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def page = to(ListBugPage, id)
        page.searchModule.search('bug')

        expect:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'bug'

        when:
        page.searchModule.resetSearch()

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == ''
    }

    void "blank value when related items are null"() {
        given:
        def person = personService.list(max:1).first()
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        bugService.save(new Bug(name: "platform testing bug II", project: project, person: person,
                status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to ListBugPage, project.id

        then:
        page.listTable.getValueInColumn(0, "Area") == ""
        page.listTable.getValueInColumn(0, "Platform") == ""
    }

    void "name displays for related items"() {
        given:
        def person = personService.list(max:1).first()
        def platform = new Platform(name: "platform testing platform II")
        def area = new Area(name: "platform testing area II")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform],
                                areas: [area]))
        bugService.save(new Bug(name: "platform testing bug II", project: project, person: person,
                platform: platform, area: area, status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to ListBugPage, project.id

        then:
        page.listTable.getValueInColumn(0, "Area") == "platform testing area II"
        page.listTable.getValueInColumn(0, "Platform") == "platform testing platform II"
    }
}
