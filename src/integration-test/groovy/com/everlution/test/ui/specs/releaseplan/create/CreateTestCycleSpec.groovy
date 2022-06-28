package com.everlution.test.ui.specs.releaseplan.create

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateTestCycleSpec extends GebSpec {

    void "authorized users can create test cycle"(String username, String password) {
        given: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        def plan = DataFactory.createReleasePlan()
        ShowReleasePlanPage page = to(ShowReleasePlanPage, plan.project.id, plan.id)

        when: "create test cycle"
        page.createTestCycle()

        then: "at show page"
        at ShowReleasePlanPage

        where:
        username                         | password
        Credentials.BASIC.email          | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email  | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email      | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email      | Credentials.APP_ADMIN.password
    }
}
