package com.everlution.test.ui.functional.specs.bug

import com.everlution.ProjectService
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    ProjectService projectService

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
        page.listTable.getHeaders() == ["Name", "Description", "Person", "Platform", "Status"]
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
        bugsPage.search('bug')

        then:
        bugsPage.listTable.rowCount > 0
        bugsPage.nameInput.text == 'bug'
    }
}
