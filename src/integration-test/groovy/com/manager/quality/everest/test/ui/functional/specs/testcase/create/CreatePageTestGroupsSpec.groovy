package com.manager.quality.everest.test.ui.functional.specs.testcase.create

import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePageTestGroupsSpec extends GebSpec {

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

    void "test groups field has no value set"() {
        when: "project is selected"
        def page = browser.page(CreateTestCasePage)

        then: "default text selected"
        page.testGroupsSelect().selectedText == []
        page.testGroupsSelect().selected == []
    }

    void "test groups field defaults with no group option"() {
        expect: "default text present"
        def page = browser.page(CreateTestCasePage)
        page.testGroupsOptions[0].text() == "Select Test Groups..."
    }
}
