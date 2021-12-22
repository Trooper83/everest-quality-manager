package com.everlution.test.ui.specs.releaseplan

import com.everlution.ReleasePlanService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.EditReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class ShowPageSpec extends GebSpec {

    ReleasePlanService releasePlanService

    @Shared int id

    def setup() {
        id = releasePlanService.list(max: 1).first().id
    }

    void "status message displayed after plan created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        def page = to CreateReleasePlanPage

        when: "create a plan"
        page.createReleasePlan()

        then: "at show page with message displayed"
        def showPage = at ShowReleasePlanPage
        showPage.statusMessage.text() ==~ /ReleasePlan \d+ created/
    }

    void "home link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to bug"
        go "/releasePlan/show/${id}"

        when: "click the home button"
        def page = browser.page(ShowReleasePlanPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to bug"
        go "/releasePlan/show/${id}"

        when: "click the list link"
        def page = browser.page(ShowReleasePlanPage)
        page.goToList()

        then: "at the list page"
        at ListReleasePlanPage
    }

    void "create link directs to create view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to plan"
        go "/releasePlan/show/${id}"

        when: "click the new bug link"
        def page = browser.page(ShowReleasePlanPage)
        page.goToCreate()

        then: "at the create page"
        at CreateReleasePlanPage
    }

    void "edit link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to bug"
        go "/releasePlan/show/${id}"

        when: "click the edit button"
        def page = browser.page(ShowReleasePlanPage)
        page.goToEdit()

        then: "at the edit page"
        at EditReleasePlanPage
    }

    void "create delete edit buttons not displayed for Read Only user"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to bug"
        go "/releasePlan/show/${id}"

        then: "create delete edit buttons are not displayed"
        def page = browser.page(ShowReleasePlanPage)
        verifyAll {
            !page.createLink.displayed
            !page.deleteLink.displayed
            !page.editLink.displayed
        }
    }

    void "create delete edit buttons displayed for authorized users"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to bug"
        go "/releasePlan/show/${id}"

        then: "create delete edit buttons are not displayed"
        def page = browser.page(ShowReleasePlanPage)
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
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to bug"
        go "/releasePlan/show/${id}"

        then: "correct fields are displayed"
        def page = browser.page(ShowReleasePlanPage)
        page.getFields() == ["Name", "Project"]
    }

    void "plan not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to plan"
        go "/releasePlan/show/${id}"

        when: "click delete and cancel | verify message"
        def showPage = browser.page(ShowReleasePlanPage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show page"
        at ShowReleasePlanPage
    }

    void "updated message displays after updating plan"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/releasePlan/edit/${id}"

        when: "edit a plan"
        def page = browser.page(EditReleasePlanPage)
        page.editReleasePlan()

        then: "at show page with message displayed"
        def showPage = at ShowReleasePlanPage
        showPage.statusMessage.text() == "ReleasePlan ${id} updated"
    }
}