package com.everlution.test.ui.functional.specs.project.home

import com.everlution.services.project.ProjectService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class HomeAdminSpec extends GebSpec {

    @Shared int id

    ProjectService projectService

    def setup() {
        id = projectService.list(max: 1).first().id
    }

    void "view button displays for basic and above"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when: "go to show project page"
        go "/project/home/${id}"

        then:
        def page = browser.at(ProjectHomePage)
        page.viewButton.displayed

        where:
        email                           | password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
        Credentials.BASIC.email         | Credentials.BASIC.password
    }

    void "view button not displayed for read only"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when: "go to show project page"
        go "/project/home/${id}"

        then:
        def page = browser.at(ProjectHomePage)
        !page.viewButton.displayed

        where:
        email                           | password
        Credentials.READ_ONLY.email     | Credentials.READ_ONLY.password
    }
}
