package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.CreateTestCasePage
import com.everlution.test.ui.support.pages.EditTestCasePage
import com.everlution.test.ui.support.pages.LoginPage
import com.everlution.test.ui.support.pages.ListTestCasePage
import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

@Integration
@Rollback
class PermissionsSpec extends GebSpec {

    void "create button not displayed on list for Read Only user"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

        when:
        to ListTestCasePage

        then:
        ListTestCasePage page = browser.page(ListTestCasePage)
        !page.isCreateButtonDisplayed()
    }

    void "create button displayed on list for users"(String username, String password) {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        when:
        to ListTestCasePage

        then:
        ListTestCasePage page = browser.page(ListTestCasePage)
        page.isCreateButtonDisplayed()

        where:
        username        | password
        "basic"         | "password"
        "project_admin" | "password"
        "org_admin"     | "password"
        "app_admin"     | "password"
    }

    void "create view displays unauthorized error for read_only user"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

        when:
        go "/testCase/create"

        then:
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.errorText.text() == "Sorry, you're not authorized to view this page."
    }

    void "edit view displays unauthorized error for read_only user"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

        when:
        go "/testCase/edit"

        then:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.errorText.text() == "Sorry, you're not authorized to view this page."
    }
}
