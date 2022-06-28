package com.everlution.test.ui.specs.bug

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.DeniedPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.common.NotFoundPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ErrorsSpec extends GebSpec {

    void "404 message displayed for not found bug"(String url) {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to show page for not found bug"
        go url

        then:
        NotFoundPage notFoundPage = at NotFoundPage
        notFoundPage.errors*.text().contains("Error: Page Not Found (404)")

        where:
        url << ["/project/1/bug/show/9999999999999999", "/project/1/bug/edit/9999999999999999"]
    }

    void "denied page displayed for read_only user"(String url) {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when: "go to create bug page"
        go url

        then: "unauthorized message is displayed"
        DeniedPage page = at DeniedPage
        page.errors.text() == "Sorry, you're not authorized to view this page."

        where:
        url << ["/project/1/bug/create", "/project/1/bug/edit/1"]
    }
}
