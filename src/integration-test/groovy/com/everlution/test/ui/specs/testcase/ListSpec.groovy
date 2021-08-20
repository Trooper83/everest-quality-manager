package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.LoginPage
import com.everlution.test.ui.support.pages.TestCaseIndexPage
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
        to TestCaseIndexPage

        then:
        def expected = ["Name", "Creator", "Type", "Execution Method"]
        TestCaseIndexPage page = browser.page(TestCaseIndexPage)
        page.testCaseTable.getHeaders() == expected
    }
}
