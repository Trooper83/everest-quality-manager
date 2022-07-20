package com.everlution.test.ui.specs.scenario.edit

import com.everlution.ScenarioService
import com.everlution.test.ui.support.data.Credentials

import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageSpec extends GebSpec {

    ScenarioService scenarioService
    @Shared int id

    def setup() {
        id = scenarioService.list(max:1).first().id

        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        browser.page(ListProjectPage).projectTable.clickCell("Name", 0)
        browser.page(ProjectHomePage).sideBar.goToProjectDomain("Scenarios")
        browser.page(ListScenarioPage).listTable.clickCell("Name", 0)
        browser.page(ShowScenarioPage).goToEdit()
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(EditScenarioPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }

    void "verify method and type field options"() {
        expect: "correct options populate for executionMethod and type"
        EditScenarioPage page = browser.page(EditScenarioPage)
        verifyAll {
            page.executionMethodOptions*.text() == ["", "Automated", "Manual"]
            page.typeOptions*.text() == ["", "UI", "API"]
        }
    }

    void "verify platform field options"() {
        expect: "correct options populate"
        EditScenarioPage page = browser.page(EditScenarioPage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }
    }
}
