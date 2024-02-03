package com.everlution.test.ui.functional.specs.common

import com.everlution.test.support.data.Credentials
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
        Credentials.BASIC.email     | Credentials.BASIC.password
        Credentials.READ_ONLY.email |Credentials.READ_ONLY.password
    }

    void "user and project options displayed for app admin"() {
        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        then:
        def projects = at ListProjectPage
        projects.navBar.isOptionDisplayed("Create User")
        projects.navBar.isOptionDisplayed("Search Users")
    }

    void "user options hidden for org and project admin"(String username, String password) {
        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        then:
        def projects = at ListProjectPage
        !projects.navBar.isOptionDisplayed("Create User")
        !projects.navBar.isOptionDisplayed("Search Users")

        where:
        username                         | password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
    }

    void "project options displayed for project admin and above"(String username, String password) {
        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        then:
        def projects = at ListProjectPage
        projects.navBar.isOptionDisplayed("Create Project")

        where:
        username                         | password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }
}
