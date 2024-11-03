package com.manager.quality.everest.test.ui.functional.specs.project

import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.DeniedPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
@SendResults
class ShowPageSpec extends GebSpec {

    @Shared int id

    ProjectService projectService

    def setup() {
        id = projectService.list(max: 1).first().id
    }

    void "edit button displayed for project admin and above"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        to(ShowProjectPage, id)

        then:
        def page = at ShowProjectPage
        page.editLink.displayed

        where:
        email                           | password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "edit button not displayed for basic"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        to(ShowProjectPage, id)

        then:
        def page = at ShowProjectPage
        !page.editLink.displayed

        where:
        email                           | password
        Credentials.BASIC.email         | Credentials.BASIC.password
    }

    void "read only cannot access show project page"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        go "/project/show/${id}"

        then:
        at DeniedPage

        where:
        email                           | password
        Credentials.READ_ONLY.email     | Credentials.READ_ONLY.password
    }
}
