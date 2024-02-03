package com.everlution.test.ui.functional.specs.testgroup.create

import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateTestGroupSpec extends GebSpec {

    ProjectService projectService

    void "authorized users can create test group"(String username, String password) {
        given: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        def id = projectService.list(max: 1).first().id
        def page = to (CreateTestGroupPage, id)

        when: "create"
        page.createTestGroup()

        then: "at show page"
        at ShowTestGroupPage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all create form data persists"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to the create page & create instance"
        def id = projectService.list(max: 1).first().id
        def page = to (CreateTestGroupPage, id)
        def name = DataFactory.testGroup().name
        page.createTestGroup(name)

        then: "at show page"
        def show = at ShowTestGroupPage
        verifyAll {
            show.nameValue.text() == name + ' Details'
        }
    }
}
