package com.everlution.test.ui.functional.specs.common

import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class AuthSpec extends GebSpec {

    void "invalid login displays message"(String username, String password) {
        given: "login with invalid credentials"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        expect: "failed login message is displayed"
        waitFor {
            loginPage.loginFailureMessage.text() == "Sorry, we were not able to find a user with that username and password."
        }

        where: "invalid credentials"
        username | password
        "fail@failing.com"| "!2022Password"
        "basic@basic.com" | "pass"
    }

    void "login with locked or expired user displays message"(String username, String password, String message) {
        given: "login with invalid credentials"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        expect: "failed login message is displayed"
        waitFor {
            loginPage.loginFailureMessage.text() == message
        }

        where: "valid credentials"
        username | password | message
        Credentials.ACCOUNT_EXPIRED.email  | Credentials.ACCOUNT_EXPIRED.password | "Sorry, your account has expired."
        Credentials.ACCOUNT_LOCKED.email   | Credentials.ACCOUNT_LOCKED.password | "Sorry, your account is locked."
        Credentials.PASSWORD_EXPIRED.email |Credentials.PASSWORD_EXPIRED.password | "Sorry, your password has expired."
        Credentials.DISABLED.email         | Credentials.DISABLED.password | "Sorry, your account is disabled."
    }

    void "user can logout"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "logout of app"
        def page = browser.page(ListProjectPage)
        page.navBar.logout()

        then: "at home page"
        at LoginPage
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
