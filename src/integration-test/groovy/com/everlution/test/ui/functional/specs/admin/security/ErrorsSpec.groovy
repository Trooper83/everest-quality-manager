package com.everlution.test.ui.functional.specs.admin.security

import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.DeniedPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ErrorsSpec extends GebSpec {

    void "denied page displayed for unauthorized users of user search view"(String url, String username) {
        given: "login as user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, "!Password#2022")

        when: "go to page"
        go url

        then: "unauthorized message is displayed"
        DeniedPage page = at DeniedPage
        page.errors.text() == "Sorry, you're not authorized to view this page."

        where:
        url            | username
        "/user/create" | Credentials.BASIC.email
        "/user/create" | Credentials.READ_ONLY.email
        "/user/create" | Credentials.PROJECT_ADMIN.email
        "/user/edit/1" | Credentials.BASIC.email
        "/user/edit/1" | Credentials.READ_ONLY.email
        "/user/edit/1" | Credentials.PROJECT_ADMIN.email
        "/user/search" | Credentials.BASIC.email
        "/user/search" | Credentials.READ_ONLY.email
        "/user/search" | Credentials.PROJECT_ADMIN.email
    }

    void "denied page displayed for non-used security views"(String url, String username) {
        given: "login as user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, "!Password#2022")

        when: "go to page"
        go url

        then: "unauthorized message is displayed"
        DeniedPage page = at DeniedPage
        page.errors.text() == "Sorry, you're not authorized to view this page."

        where:
        url            | username
        "/aclClass/create" | Credentials.APP_ADMIN.email
        "/aclClass/search" | Credentials.APP_ADMIN.email
        "/aclEntry/create" | Credentials.APP_ADMIN.email
        "/aclEntry/create" | Credentials.APP_ADMIN.email
        "/persistentLogin/search" | Credentials.APP_ADMIN.email
        "/persistentLogin/edit" | Credentials.APP_ADMIN.email
        "/register/forgotPassword" | Credentials.APP_ADMIN.email
        "/register/register" | Credentials.APP_ADMIN.email
        "/register/resetPassword" | Credentials.APP_ADMIN.email
        "/registrationCode/search" | Credentials.APP_ADMIN.email
        "/registrationCode/edit" | Credentials.APP_ADMIN.email
        "/requestmap/create" | Credentials.APP_ADMIN.email
        "/requestmap/edit" | Credentials.APP_ADMIN.email
        "/requestmap/search" | Credentials.APP_ADMIN.email
        "/securityInfo/config" | Credentials.APP_ADMIN.email
        "/securityInfo/usercache" | Credentials.APP_ADMIN.email
    }
}
