package com.manager.quality.everest.test.ui.functional.specs.testcase


import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.DeniedPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.common.NotFoundPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ErrorsSpec extends GebSpec {

    void "404 message displayed for not found test case"(String url) {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to show page for not found test case"
        go url

        then:
        NotFoundPage notFoundPage = at NotFoundPage
        notFoundPage.errors*.text().contains("Error: Page Not Found (404)")

        where:
        url << ["project/1/testCase/show/9999999999999999", "project/1/testCase/edit/9999999999999999"]
    }

    void "denied page displayed for read_only user"(String url) {
        given: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when: "go to create test case page"
        go url

        then: "unauthorized message is displayed"
        DeniedPage page = at DeniedPage
        page.errors.text() == "Sorry, you're not authorized to view this page."

        where:
        url << ["project/1/testCase/create", "project/1/testCase/edit/1"]
    }
}
