package com.everlution.test.ui.functional.specs.testgroup

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListCreateSpec extends GebSpec {

    ProjectService projectService

    void "create button not displayed for read only"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        def id = projectService.list().first().id
        go "/project/${id}/testGroups"

        then:
        def page = at ListTestGroupPage
        !page.createButton.displayed

        where:
        email                           | password
        Credentials.READ_ONLY.email     | Credentials.READ_ONLY.password
    }

    void "create button displayed for basic and above"(String email, String password) {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(email, password)

        when:
        def id = projectService.list().first().id
        go "/project/${id}/testGroups"

        then:
        def page = at ListTestGroupPage
        page.createButton.displayed

        where:
        email                           | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
    }
}
