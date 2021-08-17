package com.everlution.test.ui.specs

import com.everlution.test.ui.support.pages.HomePage
import com.everlution.test.ui.support.pages.LoginPage
import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration


@Integration
@Rollback
class TestCaseSpec extends GebSpec {

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")
    }

    def cleanup() {
    }

    void "test something"() {
        expect:
        Thread.sleep(5000)
        assert page(HomePage).title == "Welcome to Grails"

    }
}
