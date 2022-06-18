package com.everlution.test.ui.specs.common

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class AdminNavSpec extends GebSpec {

    void "admin button hidden for basic and read only users"(String username, String password) {
        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        then:
        def projects = at ListProjectPage
        !projects.navBar.adminButton.displayed

        where:
        username                     | password
        Usernames.BASIC.username     | "password"
        Usernames.READ_ONLY.username | "password"
    }

    void "user and project options displayed for app admin"() {
        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        then:
        def projects = at ListProjectPage
        projects.navBar.isOptionDisplayed("Create User")
        projects.navBar.isOptionDisplayed("Search Users")
    }

    void "user options hidden for org and project admin"(String username, String password) {
        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, "password")

        then:
        def projects = at ListProjectPage
        !projects.navBar.isOptionDisplayed("Create User")
        !projects.navBar.isOptionDisplayed("Search Users")

        where:
        username                         | password
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.PROJECT_ADMIN.username | "password"
    }

    void "project options displayed for project admin and above"(String username, String password) {
        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, "password")

        then:
        def projects = at ListProjectPage
        projects.navBar.isOptionDisplayed("Create Project")

        where:
        username                         | password
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.APP_ADMIN.username     | "password"
    }
}
