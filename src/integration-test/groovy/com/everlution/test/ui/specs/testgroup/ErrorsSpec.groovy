package com.everlution.test.ui.specs.testgroup

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.DeniedPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.common.NotFoundPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ErrorsSpec extends GebSpec {

    void "404 message displayed for not found"(String url) {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to show page for not found"
        go url

        then:
        NotFoundPage notFoundPage = at NotFoundPage
        notFoundPage.errors*.text().contains("Error: Page Not Found (404)")

        where:
        url << ["/project/1/testGroup/show/9999999999999999", "/project/1/testGroup/edit/9999999999999999"]
    }

    void "denied page displayed for read_only user"(String url) {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when: "go to page"
        go url

        then: "unauthorized message is displayed"
        DeniedPage page = at DeniedPage
        page.errors.text() == "Sorry, you're not authorized to view this page."

        where:
        url << ["/project/1/testGroup/create", "/project/1/testGroup/edit"]
    }
}
