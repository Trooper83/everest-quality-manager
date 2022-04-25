package com.everlution.test.ui.specs.releaseplan.create

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        def projectId = projectService.list(max: 1).first().id
        to(CreateReleasePlanPage, projectId)
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        def page = browser.page(CreateReleasePlanPage)
        page.getFields() == ["Project", "Name *"]
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(CreateReleasePlanPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }
}
