package com.everlution.test.ui.functional.specs.testgroup

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

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
        page.listTable.getHeaders() == ["Name"]
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
        page.search('test')

        then:
        page.listTable.rowCount > 0
        page.nameInput.text == 'test'
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
        def groups = testGroupService.findAllByProject(project, [max:20])
        if (groups.count < 12) {
            def c = 12 - groups.count
            for (int i = 0; i <= c; i++) {
                def g = DataFactory.testGroup()
                testGroupService.save(new TestGroup(name: g.name, project: project))
            }
        }

        def page = at ListTestGroupPage
        refreshWaitFor {
            page.listTable.getRowCount() == 10
        }

        when:
        page.scrollToBottom()
        def found = page.listTable.getValueInColumn(0, 'Name')
        page.listTable.goToPage('2')

        then:
        at ListTestGroupPage
        !page.listTable.isValueInColumn('Name', found)
    }
}
