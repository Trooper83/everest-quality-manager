package com.everlution.test.ui.specs.bug

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        to CreateBugPage
    }

    void "home link directs to home view"() {
        when: "click the home button"
        def page = browser.page(CreateBugPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "click the list link"
        def page = browser.page(CreateBugPage)
        page.goToList()

        then: "at the list page"
        at ListBugPage
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        def page = browser.page(CreateBugPage)
        page.getFields() == ["Description", "Name *", "Project *"]
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(CreateBugPage)
        page.areRequiredFieldIndicatorsDisplayed(["project", "name"])
    }

    void "add test step row"() {
        expect: "row count is 0"
        def page = browser.page(CreateBugPage)
        page.stepsTable.getRowCount() == 0

        when: "add a test step row"
        page.stepsTable.addRow()

        then: "row count is 1"
        page.stepsTable.getRowCount() == 1
    }

    void "remove test step row"() {
        setup: "add a row"
        def page = browser.page(CreateBugPage)
        page.stepsTable.addRow()

        expect: "row count is 1"
        page.stepsTable.getRowCount() == 1

        when: "remove the first row"
        page.stepsTable.removeRow(0)

        then: "row count is 0"
        page.stepsTable.getRowCount() == 0
    }
}
