package com.everlution.test.ui.specs.admin.user

import com.everlution.test.ui.support.data.UserStatuses
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.admin.user.EditUserPage
import com.everlution.test.ui.support.pages.admin.user.SearchUserPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class SearchUserSpec extends GebSpec {

    void "searching for user by email and enabled returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.ENABLED.status): 'true']
        page.search("read_only@readonly.com", map)

        then:
        page.isValueInColumn("Email", "read_only@readonly.com")
        page.getRowCount() == 1
    }

    void "searching for user by account expired returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.ACCOUNT_EXPIRED.status): 'true']
        page.search("", map)

        then:
        page.isValueInColumn("Email", "expired@accountexpired.com")
        page.getRowCount() == 1
    }

    void "searching for user by account locked returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.ACCOUNT_LOCKED.status): 'true']
        page.search("", map)

        then:
        page.isValueInColumn("Email", "locked@accountlocked.com")
    }

    void "searching for user by password expired returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.PASSWORD_EXPIRED.status): 'true']
        page.search("", map)

        then:
        page.isValueInColumn("Email", "expired@passwordexpired.com")
        page.getRowCount() == 1
    }

    void "searching for user by inactive returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.ENABLED.status): 'false']
        page.search("", map)

        then:
        page.isValueInColumn("Email", "disabled@disabled.com")
    }

    void "email link redirects to edit view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to SearchUserPage

        when:
        page.search("", [:])
        page.clickCell("Email", 0)

        then:
        at EditUserPage
    }
}
