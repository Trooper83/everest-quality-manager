package com.everlution.test.ui.specs.project

import com.everlution.test.ui.support.pages.common.DeniedPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.common.NotFoundPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ErrorsSpec extends GebSpec {

    void "404 message displayed for not found project"(String url) {
        given: "login as project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("project_admin", "password")

        when: "go to show page for not found project"
        go url

        then:
        NotFoundPage notFoundPage = at NotFoundPage
        notFoundPage.errors*.text().contains("Error: Page Not Found (404)")

        where:
        url << ["/project/show/", "/project/edit/"]
    }

    void "denied page displayed for read_only and basic user"(String url, String username) {
        given: "login as user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, "password")

        when: "go to page"
        go url

        then: "unauthorized message is displayed"
        DeniedPage page = at DeniedPage
        page.errors.text() == "Sorry, you're not authorized to view this page."

        where:
        url               | username
        "/project/create" | "basic"
        "/project/create" | "read_only"
        "/project/edit"   | "basic"
        "/project/edit"   | "read_only"
        "/project/show"   | "basic"
        "/project/show"   | "read_only"
        "/project/index"  | "basic"
        "/project/index"  | "read_only"
    }
}
