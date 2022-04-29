package com.everlution.test.ui.specs.bug

import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToListsPage('Bugs')

        when: "go to list page"
        def page = at ListBugPage

        then: "correct headers are displayed"
        page.bugTable.getHeaders() == ["Name", "Description", "Person", "Project", "Platform"]
    }

    void "delete message displays after bug deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToListsPage('Bugs')

        and: "go to list page"
        def page = at ListBugPage
        page.bugTable.clickCell('Name', 0)

        when: "delete bug"
        def showPage = browser.page(ShowBugPage)
        showPage.deleteBug()

        then: "at list page and message displayed"
        def listPage = at ListBugPage
        listPage.statusMessage.text() ==~ /Bug \d+ deleted/
    }
}
