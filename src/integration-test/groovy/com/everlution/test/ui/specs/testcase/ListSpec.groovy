package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.CreateTestCasePage
import com.everlution.test.ui.support.pages.HomePage
import com.everlution.test.ui.support.pages.LoginPage
import com.everlution.test.ui.support.pages.ListTestCasePage
import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

@Integration
@Rollback
class ListSpec extends GebSpec {

    void "verify list table headers order"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

        when:
        to ListTestCasePage

        then:
        def expected = ["Name", "Creator", "Type", "Execution Method"]
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.testCaseTable.getHeaders() == expected
    }

    void "home link directs to home view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")
        to ListTestCasePage

        when:
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.goToHome()

        then:
        at HomePage
    }

    void "new test case link directs to create view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")
        to ListTestCasePage

        when:
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.goToCreateTestCase()

        then:
        at CreateTestCasePage
    }
}
