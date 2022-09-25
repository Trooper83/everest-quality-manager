package com.everlution.test.ui.functional.specs.project

import com.everlution.test.ui.support.data.Credentials
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
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        when: "go to show page for not found project"
        go url

        then:
        NotFoundPage notFoundPage = at NotFoundPage
        notFoundPage.errors*.text().contains("Error: Page Not Found (404)")

        where:
        url << ["/project/home/99999999999", "/project/edit/999999999999"]
    }

    void "denied page displayed for read_only and basic user"(String url, String username, String password) {
        given: "login as user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        when: "go to page"
        go url

        then: "unauthorized message is displayed"
        DeniedPage page = at DeniedPage
        page.errors.text() == "Sorry, you're not authorized to view this page."

        where:
        url               | username                    | password
        "/project/create" | Credentials.BASIC.email     | Credentials.BASIC.password
        "/project/create" | Credentials.READ_ONLY.email | Credentials.READ_ONLY.password
        "/project/edit/1" | Credentials.BASIC.email     | Credentials.BASIC.password
        "/project/edit/1" | Credentials.READ_ONLY.email | Credentials.READ_ONLY.password
    }
}
