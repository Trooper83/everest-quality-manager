package com.everlution.test.ui.functional.specs.admin.user

import com.everlution.domains.Person
import com.everlution.services.person.SpringSecurityUiService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.UserStatuses
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.admin.user.EditUserPage
import com.everlution.test.ui.support.pages.admin.user.SearchUserPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class SearchUserSpec extends GebSpec {

    SpringSecurityUiService springSecurityUiService

    void "searching for user by email and enabled returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.ENABLED.status): 'true']
        page.search("read_only@readonly.com", map)

        then:
        page.resultsTable.isValueInColumn("Email", "read_only@readonly.com")
        page.resultsTable.getRowCount() == 1
    }

    void "searching for user by account expired returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.ACCOUNT_EXPIRED.status): 'true']
        page.search("", map)

        then:
        page.resultsTable.isValueInColumn("Email", "expired@accountexpired.com")
        page.resultsTable.getRowCount() == 1
    }

    void "searching for user by account locked returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.ACCOUNT_LOCKED.status): 'true']
        page.search("", map)

        then:
        page.resultsTable.isValueInColumn("Email", "locked@accountlocked.com")
    }

    void "searching for user by password expired returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.PASSWORD_EXPIRED.status): 'true']
        page.search("", map)

        then:
        page.resultsTable.isValueInColumn("Email", "expired@passwordexpired.com")
        page.resultsTable.getRowCount() == 1
    }

    void "searching for user by inactive returns users"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        and:
        def page = to SearchUserPage

        when:
        def map = [(UserStatuses.ENABLED.status): 'false']
        page.search("", map)

        then:
        page.resultsTable.isValueInColumn("Email", "disabled@disabled.com")
    }

    void "email link redirects to edit view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        and:
        def page = to SearchUserPage

        when:
        page.search("", [:])
        page.resultsTable.clickCell("Email", 0)

        then:
        at EditUserPage
    }

    void "pagination for search users works"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)
        def page = to SearchUserPage
        page.search("", [:])
        waitFor {
            page.resultsTable.getRowCount() > 0
        }
        if (page.resultsTable.getRowCount() < 12) {
            def c = 12 - page.resultsTable.getRowCount()

            for (int i = 0; i <= c; i++) {
                def p = DataFactory.person()
                springSecurityUiService.saveUser([email: p.email],[], p.password) as Person
            }
        }

        page.search("", [:])

        expect:
        page.resultsTable.getRowCount() == 10
        page.resultsTable.paginationGroup.displayed

        when:
        page.scrollToBottom()
        page.resultsTable.goToPage("2")

        then:
        at SearchUserPage
        page.resultsTable.displayed
    }
}
