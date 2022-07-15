package com.everlution.test.ui.specs.releaseplan.create

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class CreateReleasePlanSpec extends GebSpec {

    ProjectService projectService

    @Shared Project project

    def setup() {
        project = projectService.list(max: 1).first()
    }

    void "authorized users can create plan"(String username, String password) {
        given: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        def page = to(CreateReleasePlanPage, project.id)

        when: "create a bug"
        page.createReleasePlan()

        then: "at show page"
        at ShowReleasePlanPage

        where:
        username                         | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all create form data persists"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to the create page & create instance"
        def page = to(CreateReleasePlanPage, project.id)
        def name = DataFactory.releasePlan().name
        page.createReleasePlan(name)

        then: "at show page"
        def show = at ShowReleasePlanPage
        verifyAll {
            show.nameValue.text() == name
        }
    }
}
