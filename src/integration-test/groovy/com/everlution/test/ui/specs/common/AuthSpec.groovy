package com.everlution.test.ui.specs.common

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class AuthSpec extends GebSpec {

    void "invalid login displays message"(String username, String password) {
        given: "login with invalid credentials"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        expect: "failed login message is displayed"
        loginPage.loginFailureMessage.text() == "Sorry, we were not able to find a user with that username and password."

        where: "invalid credentials"
        username | password
        "fail"| "password"
        "basic" | "pass"
        " " | " "
    }

    void "welcome message displays for logged in user"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        expect: "welcome message is displayed"
        HomePage homePage = browser.page(HomePage)
        homePage.navBar.verifyWelcomeMessage("basic")
    }

    void "user can logout"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "logout of app"
        HomePage homePage = browser.page(HomePage)
        homePage.navBar.logout()

        then: "at home page"
        at HomePage
    }

    void "welcome message not displayed for non-logged in user"() {
        expect: "welcome message is not displayed"
        HomePage homePage = browser.page(HomePage)
        !homePage.navBar.isWelcomeMessageDisplayed()
    }

    void "anonymous user redirected to login page"(String url) {
        when: "go to protected test case pages"
        go url

        then: "at login page"
        at LoginPage

        where: "protected test case page urls"
        url << ["/testCase/index", "/testCase/create", "/testCase/edit", "/testCase/show"]
    }
}
