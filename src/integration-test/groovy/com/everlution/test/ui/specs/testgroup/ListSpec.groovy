package com.everlution.test.ui.specs.testgroup

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToListsPage('Test Groups')
    }

    void "verify list table headers order"() {
        expect: "correct headers are displayed"
        def page = browser.page(ListTestGroupPage)
        page.listTable.getHeaders() == ["Name", "Project"]
    }

    void "clicking name column directs to show page"() {
        when: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowTestGroupPage
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
