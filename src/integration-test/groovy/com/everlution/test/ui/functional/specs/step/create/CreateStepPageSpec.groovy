package com.everlution.test.ui.functional.specs.step.create

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.step.CreateStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateStepPageSpec extends GebSpec {

    void "error message displays when name null"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Step")

        when: "create bug"
        def createPage = browser.page(CreateStepPage)
        createPage.createStep("action", "", "result")

        then:
        createPage.errorsMessage.displayed
    }
}
