package com.everlution.test.ui.specs.common

import com.everlution.test.ui.support.pages.common.DeniedPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.common.NotFoundPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ErrorsSpec extends GebSpec {

    void "404 message displayed for not found test case"(String url) {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("basic", "password")

        when: "go to show page for not found test case"
        go url

        then:
        at NotFoundPage
        NotFoundPage notFoundPage = browser.page(NotFoundPage)
        notFoundPage.errors*.text().contains("Error: Page Not Found (404)")

        where:
        url << ["/testCase/show/9999999999999999", "/testCase/edit/9999999999999999"]
    }

    void "denied page displayed for read_only user"(String url) {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

        when: "go to create test case page"
        go url

        then: "unauthorized message is displayed"
        DeniedPage page = browser.page(DeniedPage)
        page.errors.text() == "Sorry, you're not authorized to view this page."

        where:
        url << ["/testCase/create", "/testCase/edit"]
    }
}
