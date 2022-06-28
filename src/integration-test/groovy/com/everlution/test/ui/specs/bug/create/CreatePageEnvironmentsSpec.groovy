package com.everlution.test.ui.specs.bug.create

import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageEnvironmentsSpec extends GebSpec {

    def setup() {
        setup: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToCreatePage('Bug')
    }

    void "environments field has no value set"() {
        when: "project is selected"
        def page = browser.page(CreateBugPage)

        then: "default text selected"
        page.environmentsSelect().selectedText == []
        page.environmentsSelect().selected == []
        page.environmentsOptions[0].text() == "Select Environments..."
    }
}
