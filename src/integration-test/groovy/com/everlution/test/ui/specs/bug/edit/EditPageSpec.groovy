package com.everlution.test.ui.specs.bug.edit

import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    void "correct fields are displayed"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToListsPage('Bugs')

        when: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.bugTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        then: "correct fields are displayed"
        def page = browser.page(EditBugPage)
        page.getFields() == ["Project", "Area", "Environments", "Description", "Name *", "Platform"]
    }

    void "required fields indicator displayed for required fields"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToListsPage('Bugs')

        when: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.bugTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        then: "required field indicators displayed"
        def page = browser.page(EditBugPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }

    void "verify platform options"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToListsPage('Bugs')

        when: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.bugTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        then: "correct options are populated"
        EditBugPage page = browser.page(EditBugPage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }
    }
}
