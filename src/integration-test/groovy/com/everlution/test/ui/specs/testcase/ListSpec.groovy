package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

@Integration
@Rollback
class ListSpec extends GebSpec {

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

        when: "go to list test case page"
        to ListTestCasePage

        then: "correct headers are displayed"
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.testCaseTable.getHeaders() == ["Name", "Creator", "Type", "Execution Method"]
    }

    void "home link directs to home view"() {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

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
        loginPage.login("basic", "password")

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
        loginPage.login("read_only", "password")

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
        username        | password
        "basic"         | "password"
        "project_admin" | "password"
        "org_admin"     | "password"
        "app_admin"     | "password"
    }
}
