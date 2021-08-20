package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.LoginPage
import com.everlution.test.ui.support.pages.TestCaseIndexPage
import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

@Integration
@Rollback
class PermissionsSpec extends GebSpec {

    void "create button not displayed on index for Read Only user"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

        when:
        to TestCaseIndexPage

        then:
        TestCaseIndexPage page = browser.page(TestCaseIndexPage)
        !page.isCreateButtonDisplayed()
    }

    void "create button displayed on index for users"(String username, String password) {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        when:
        to TestCaseIndexPage

        then:
        TestCaseIndexPage page = browser.page(TestCaseIndexPage)
        page.isCreateButtonDisplayed()

        where:
        username        | password
        "basic"         | "password"
        "project_admin" | "password"
        "org_admin"     | "password"
        "app_admin"     | "password"
    }
}
