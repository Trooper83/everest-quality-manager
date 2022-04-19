package com.everlution.test.ui.specs.scenario.create

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
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
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenario/create"
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        def page = browser.page(CreateScenarioPage)
        page.getFields() == ["Project", "Area", "Environments", "Gherkin", "Description",
                             "Execution Method", "Name *", "Platform", "Type"]
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(CreateScenarioPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }

    void "verify platform options"() {
        expect: "correct options are populated"
        CreateScenarioPage page = browser.page(CreateScenarioPage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }

        and: "default value is blank"
        verifyAll( {
            page.platformSelect().selected == ""
        })
    }
    void "verify method and type field options"() {
        expect: "correct options populate for executionMethod and type"
        CreateScenarioPage page = browser.page(CreateScenarioPage)
        verifyAll {
            page.executionMethodOptions*.text() == ["", "Automated", "Manual"]
            page.typeOptions*.text() == ["", "UI", "API"]
        }

        and: "default values are blank"
        verifyAll( {
            page.executionMethodSelect().selected == ""
            page.typeSelect().selected == ""
        })
    }
}
