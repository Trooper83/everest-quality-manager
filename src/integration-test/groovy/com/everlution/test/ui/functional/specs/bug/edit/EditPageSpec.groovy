package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    void "verify platform options"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        when: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        then: "correct options are populated"
        EditBugPage page = browser.page(EditBugPage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }
    }

    void "verify status options"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        when: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        then: "correct options are populated"
        EditBugPage page = browser.page(EditBugPage)
        verifyAll {
            page.statusOptions*.text() == ["Open", "Closed"]
        }
    }
}
