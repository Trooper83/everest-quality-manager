package com.everlution.test.ui.functional.specs.bug.create

import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(CreateBugPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }

    void "verify platform options"() {
        expect: "correct options are populated"
        CreateBugPage page = browser.page(CreateBugPage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }

        and: "default value is blank"
        verifyAll( {
            page.platformSelect().selected == ""
        })
    }
}
