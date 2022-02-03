package com.everlution.test.ui.specs.testcycle

import com.everlution.ReleasePlanService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import com.everlution.test.ui.support.pages.testcycle.CreateTestCyclePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class CreatePageSpec extends GebSpec {

    ReleasePlanService releasePlanService

    @Shared int id

    def setup() {
        id = releasePlanService.list(max: 1).first().id

        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        go "/testCycle/create?releasePlan.id=${id}"
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        def page = browser.page(CreateTestCyclePage)
        page.getFields() == ["Environment", "Name *", "Platform"]
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(CreateTestCyclePage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }

    void "verify platform options"() {
        expect: "correct options are populated"
        CreateTestCyclePage page = browser.page(CreateTestCyclePage)
        page.platformOptions*.text() == ["", "Android", "iOS", "Web"]

        and: "default value is blank"
        page.platformSelect().selected == ""
    }

    void "environ defaults with select text"() {
        expect:"default value is select text"
        CreateTestCyclePage page = browser.page(CreateTestCyclePage)
        page.environSelect().selectedText == "Select an Environment..."
        page.environSelect().selected == ""
    }

    void "cancel button redirects to release plan"() {
        when: "click cancel"
        CreateTestCyclePage page = browser.page(CreateTestCyclePage)
        page.goToReleasePlan()

        then: "at release plan page"
        at ShowReleasePlanPage
    }
}
