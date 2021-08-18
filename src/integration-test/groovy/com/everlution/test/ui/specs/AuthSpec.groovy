package com.everlution.test.ui.specs

import com.everlution.test.ui.support.pages.HomePage
import com.everlution.test.ui.support.pages.LoginPage
import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

@Integration
@Rollback
class AuthSpec extends GebSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "invalid login displays message"(String username, String password) {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        expect:
        loginPage.loginFailureMessage.text() == "Sorry, we were not able to find a user with that username and password."

        where:
        username | password
        "fail"| "password"
        "basic" | "pass"
        " " | " "
    }

    void "welcome message displays for logged in user"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        expect:
        HomePage homePage = browser.page(HomePage)
        homePage.navBar.verifyWelcomeMessage("basic")
    }

    void "user can logout"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        when:
        HomePage homePage = browser.page(HomePage)
        homePage.navBar.logout()

        then:
        at HomePage
    }

    void "welcome message not displayed for non-logged in user"() {
        expect:
        HomePage homePage = browser.page(HomePage)
        !homePage.navBar.isWelcomeMessageDisplayed()
    }
}
