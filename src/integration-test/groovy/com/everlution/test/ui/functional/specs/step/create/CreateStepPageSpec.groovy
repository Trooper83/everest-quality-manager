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

    void "steps are retrieved when validation fails"() {
        expect: //verify steps can be fetched with the correct url
        false
    }

    void "tooltips display when they should"() {
        expect: //TODO: this should be multiple tests
        false
    }

    void "results fetched only when three characters typed"() {
        expect:
        false //could this be done by inspecting the network?
    }

    void "validation message displayed when name not selected from list"() {
        expect:
        false
    }

    void "validation message removed when linkd added"() {
        expect:
        false
    }

    void "data-id removed from search input once linked step added"() {
        expect:
        false
    }

    void "relation field reset when linked step added"() {
        expect:
        false
    }

    void "step name search field reset when linked step added"() {
        expect:
        false
    }

    void "linked steps display"() {
        expect:
        false
    }

    void "added linked step can be removed"() {
        expect:
        false
    }
}
