package com.everlution.test.ui.specs.releaseplan.edit

import com.everlution.ReleasePlanService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.EditReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageProjectSpec extends GebSpec {

    @Shared int id

    ReleasePlanService releasePlanService

    def setup() {
        id = releasePlanService.list(max:1).first().id
    }

    void "project name tag is html span and project cannot be edited"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/releasePlan/edit/${id}"

        then: "project tag is span"
        def page = browser.page(EditReleasePlanPage)
        page.projectNameField.tag() == "span"
    }
}
