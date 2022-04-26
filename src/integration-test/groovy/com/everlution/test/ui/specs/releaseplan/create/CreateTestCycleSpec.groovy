package com.everlution.test.ui.specs.releaseplan.create

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
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
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }
}
