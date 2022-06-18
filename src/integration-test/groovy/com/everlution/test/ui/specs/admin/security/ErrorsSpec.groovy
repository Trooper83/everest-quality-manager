package com.everlution.test.ui.specs.admin.security

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.DeniedPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ErrorsSpec extends GebSpec {

    void "denied page displayed for unauthorized users of user search view"(String url, String username) {
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
        url            | username
        "/user/create" | Usernames.BASIC.username
        "/user/create" | Usernames.READ_ONLY.username
        "/user/create" | Usernames.PROJECT_ADMIN.username
        "/user/create" | Usernames.ORG_ADMIN.username
        "/user/edit/1" | Usernames.BASIC.username
        "/user/edit/1" | Usernames.READ_ONLY.username
        "/user/edit/1" | Usernames.PROJECT_ADMIN.username
        "/user/edit/1" | Usernames.ORG_ADMIN.username
        "/user/search" | Usernames.BASIC.username
        "/user/search" | Usernames.READ_ONLY.username
        "/user/search" | Usernames.PROJECT_ADMIN.username
        "/user/search" | Usernames.ORG_ADMIN.username
    }

    void "denied page displayed for non-used security views"(String url, String username) {
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
        url            | username
        "/aclClass/create" | Usernames.APP_ADMIN.username
        "/aclClass/search" | Usernames.APP_ADMIN.username
        "/aclEntry/create" | Usernames.APP_ADMIN.username
        "/aclEntry/create" | Usernames.APP_ADMIN.username
        "/persistentLogin/search" | Usernames.APP_ADMIN.username
        "/persistentLogin/edit" | Usernames.APP_ADMIN.username
        "/register/forgotPassword" | Usernames.APP_ADMIN.username
        "/register/register" | Usernames.APP_ADMIN.username
        "/register/resetPassword" | Usernames.APP_ADMIN.username
        "/registrationCode/search" | Usernames.APP_ADMIN.username
        "/registrationCode/edit" | Usernames.APP_ADMIN.username
        "/requestmap/create" | Usernames.APP_ADMIN.username
        "/requestmap/edit" | Usernames.APP_ADMIN.username
        "/requestmap/search" | Usernames.APP_ADMIN.username
        "/securityInfo/config" | Usernames.APP_ADMIN.username
        "/securityInfo/usercache" | Usernames.APP_ADMIN.username
    }
}
