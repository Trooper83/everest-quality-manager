package com.everlution.test.ui.specs.bug

import com.everlution.BugService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ShowPageSpec extends GebSpec {

    BugService bugService

    void "status message displayed after bug created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        def page = to CreateBugPage

        when: "create a bug"
        page.createBug()

        then: "at show page with message displayed"
        def showPage = at ShowBugPage
        showPage.statusMessage.text() ==~ /Bug \d+ created/
    }

    void "home link directs to home view"() {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to bug"
        go "/bug/show/${id}"

        when: "click the home button"
        def page = browser.page(ShowBugPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to bug"
        go "/bug/show/${id}"

        when: "click the list link"
        def page = browser.page(ShowBugPage)
        page.goToList()

        then: "at the list page"
        at ListBugPage
    }

    void "create link directs to create view"() {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to bug"
        go "/bug/show/${id}"

        when: "click the new bug link"
        def page = browser.page(ShowBugPage)
        page.goToCreate()

        then: "at the create page"
        at CreateBugPage
    }

    void "edit link directs to home view"() {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to bug"
        go "/bug/show/${id}"

        when: "click the edit button"
        def page = browser.page(ShowBugPage)
        page.goToEdit()

        then: "at the edit page"
        at EditBugPage
    }

    void "create delete edit buttons not displayed for Read Only user"() {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to bug"
        go "/bug/show/${id}"

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowBugPage)
        verifyAll {
            !page.createLink.displayed
            !page.deleteLink.displayed
            !page.editLink.displayed
        }
    }

    void "create delete edit buttons displayed for authorized users"(String username, String password) {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to bug"
        go "/bug/show/${id}"

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowBugPage)
        verifyAll {
            page.createLink.displayed
            page.deleteLink.displayed
            page.editLink.displayed
        }

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "correct fields are displayed"() {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to bug"
        go "/bug/show/${id}"

        then: "correct fields are displayed"
        def page = browser.page(ShowBugPage)
        page.getFields() == ["Created By", "Project", "Area", "Environments", "Name", "Description", "Platform"]
    }

    void "bug not deleted if alert is canceled"() {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to bug"
        go "/bug/show/${id}"

        when: "click delete and cancel | verify message"
        def showPage = browser.page(ShowBugPage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show bug page"
        at ShowBugPage
    }

    void "updated message displays after updating bug"() {
        setup: "get a bug id"
        def id = bugService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit bug"
        go "/bug/edit/${id}"

        when: "edit a bug"
        def page = browser.page(EditBugPage)
        page.editBug()

        then: "at show bug page with message displayed"
        def showPage = at ShowBugPage
        showPage.statusMessage.text() == "Bug ${id} updated"
    }
}
