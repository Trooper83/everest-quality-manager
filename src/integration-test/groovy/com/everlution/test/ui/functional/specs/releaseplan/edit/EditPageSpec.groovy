package com.everlution.test.ui.functional.specs.releaseplan.edit

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.EditReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def plan = DataFactory.createReleasePlan()
        to (EditReleasePlanPage, plan.project.id, plan.id)
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(EditReleasePlanPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }
}
