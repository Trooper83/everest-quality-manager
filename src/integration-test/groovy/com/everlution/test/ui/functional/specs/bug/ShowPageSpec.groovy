package com.everlution.test.ui.functional.specs.bug

import com.everlution.BugService
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ShowPageSpec extends GebSpec {

    BugService bugService

    void "status message displayed after bug created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")

        when: "create a bug"
        def page = at CreateBugPage
        page.createBug()

        then: "at show page with message displayed"
        def showPage = at ShowBugPage
        showPage.statusMessage.text() ==~ /Bug \d+ created/
    }

    void "edit link directs to home view"() {
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
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        when: "click the edit button"
        def page = browser.page(ShowBugPage)
        page.goToEdit()

        then: "at the edit page"
        at EditBugPage
    }

    void "delete edit buttons not displayed for Read Only user"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        when: "go to list page"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowBugPage)
        verifyAll {
            !page.deleteLink.displayed
            !page.editLink.displayed
        }
    }

    void "delete edit buttons displayed for authorized users"(String username, String password) {
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

        when: "go to list page"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowBugPage)
        verifyAll {
            page.deleteLink.displayed
            page.editLink.displayed
        }

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "bug not deleted if alert is canceled"() {
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
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        when: "click delete and cancel | verify message"
        def showPage = browser.page(ShowBugPage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show bug page"
        at ShowBugPage
    }

    void "updated message displays after updating bug"() {
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
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        and: "go to edit"
        def showPage = browser.page(ShowBugPage)
        showPage.goToEdit()

        when: "edit a bug"
        def page = browser.page(EditBugPage)
        page.edit()

        then: "at show bug page with message displayed"
        showPage.statusMessage.text() ==~ /Bug \d+ updated/
    }
}
