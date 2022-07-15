package com.everlution.test.ui.specs.testcase.create

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/testCase/create"
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }

    void "verify method and type field options"() {
        expect: "correct options populate for executionMethod and type"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
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

    void "verify platform options"() {
        expect: "correct options are populated"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }

        and: "default value is blank"
        verifyAll( {
            page.platformSelect().selected == ""
        })
    }
}
