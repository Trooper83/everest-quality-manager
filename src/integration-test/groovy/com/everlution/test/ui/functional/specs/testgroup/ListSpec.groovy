package com.everlution.test.ui.functional.specs.testgroup

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    ProjectService projectService

    def setup() {
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
}
