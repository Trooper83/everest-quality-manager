package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.CreateTestCasePage
import com.everlution.test.ui.support.pages.HomePage
import com.everlution.test.ui.support.pages.LoginPage
import com.everlution.test.ui.support.pages.ListTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import org.springframework.test.annotation.Rollback

@Rollback
@Integration
class CreateSpec extends GebSpec {

    void "home link directs to home view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")
        to CreateTestCasePage

        when:
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.goToHome()

        then:
        at HomePage
    }

    void "list link directs to list view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")
        to CreateTestCasePage

        when:
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.goToList()

        then:
        at ListTestCasePage
    }
}
