package com.manager.quality.everest.test.ui.functional.specs.testgroup

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.services.testgroup.TestGroupService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testgroup.ListTestGroupPage
import com.manager.quality.everest.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class ListSpec extends GebSpec {

    ProjectService projectService
    TestGroupService testGroupService

    @Shared Project project

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        project = projectService.list(max:1).first()
        to(ListTestGroupPage, project.id)
    }

    void "verify list table headers order"() {
        expect: "correct headers are displayed"
        def page = browser.page(ListTestGroupPage)
        page.listTable.getHeaders() == ["Name", "Created"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given:
        def page = browser.page(ListTestGroupPage)
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
        'Created' | 'dateCreated'
    }

    void "clicking name column directs to show page"() {
        when: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowTestGroupPage
    }

    void "search returns results"() {
        when:
        def page = at ListTestGroupPage
        page.searchModule.search('test')

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'test'
    }

    void "delete message displays after group deleted"() {
        given: "click first test group in list"
        def page = browser.page(ListTestGroupPage)
        page.listTable.clickCell("Name", 0)

        when: "delete"
        def showPage = browser.page(ShowTestGroupPage)
        showPage.delete()

        then: "at list page and message displayed"
        def listPage = at ListTestGroupPage
        listPage.statusMessage.text() ==~ /TestGroup \d+ deleted/
    }

    void "pagination loads next page"() {
        given:
        def groups = testGroupService.findAllByProject(project, [max:30])
        if (groups.count < 26) {
            for (int i = 0; i <= 26; i++) {
                def g = DataFactory.testGroup()
                testGroupService.save(new TestGroup(name: g.name, project: project))
            }
        }

        def page = at ListTestGroupPage
        refreshWaitFor {
            page.listTable.getRowCount() == 25
        }

        when:
        page.scrollToBottom()
        def found = page.listTable.getValueInColumn(0, 'Name')
        page.listTable.goToPage('2')

        then:
        at ListTestGroupPage
        !page.listTable.isValueInColumn('Name', found)
    }

    void "reset button reloads results"() {
        given: "login as a read only user"
        def page = browser.page(ListTestGroupPage)
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
}
