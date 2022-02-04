package com.everlution.test.ui.specs.testcycle

import com.everlution.ReleasePlanService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import com.everlution.test.ui.support.pages.testcycle.CreateTestCyclePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class CreateTestCycleSpec extends GebSpec {

    ReleasePlanService releasePlanService

    @Shared int id

    def setup() {
        id = releasePlanService.list(max: 1).first().id

        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        go "/testCycle/create?releasePlan.id=${id}"
    }

    void "test cycle is displayed after created"() {
        when:
        def tcd = DataFactory.testCycle()
        def page = browser.page(CreateTestCyclePage)
        page.createTestCycle(tcd.name, "1", "Web")

        then:
        def show = at ShowReleasePlanPage
        show.isTestCyclePresent(tcd.name)
    }
}
