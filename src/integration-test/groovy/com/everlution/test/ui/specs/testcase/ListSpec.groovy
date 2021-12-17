package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list test case page"
        to ListTestCasePage

        then: "correct headers are displayed"
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.testCaseTable.getHeaders() == ["Name", "Person", "Type", "Execution Method", "Project", "Platform"]
    }

    void "home link directs to home view"() {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        when: "click home button"
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.goToHome()

        then: "at home page"
        at HomePage
    }

    void "new test case link directs to create view"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        when: "go to create test case page"
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.goToCreateTestCase()

        then: "at test case page"
        at CreateTestCasePage
    }

    void "create button not displayed on list for Read Only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to list test case page"
        to ListTestCasePage

        then: "create test case button is not displayed"
        ListTestCasePage page = browser.page(ListTestCasePage)
        !page.createTestCaseLink.displayed
    }

    void "create button displayed on list for users"(String username, String password) {
        given: "login as basic and above user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        when: "go to list test case page"
        to ListTestCasePage

        then: "create test case button is displayed"
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.createTestCaseLink.displayed

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "delete message displays after test case deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to list test case page"
        to ListTestCasePage

        and: "click first test case in list"
        ListTestCasePage listPage = browser.page(ListTestCasePage)
        listPage.testCaseTable.clickCell("Name", 0)

        when: "delete test case"
        ShowTestCasePage showPage = browser.page(ShowTestCasePage)
        showPage.deleteTestCase()

        then: "at list page and message displayed"
        at ListTestCasePage
        listPage.statusMessage.text() ==~ /TestCase \d+ deleted/
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to list page"
        def listPage = to ListTestCasePage

        when: "click first bug in list"
        listPage.testCaseTable.clickCell("Name", 0)

        then: "at show page"
        at ShowTestCasePage
    }
}
