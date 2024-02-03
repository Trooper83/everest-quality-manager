package com.everlution.test.ui.functional.specs.testcase.create

import com.everlution.ProjectService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageAreaSpec extends GebSpec {

    ProjectService projectService

    void "area field has no value set"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def project = projectService.list(max: 1).first()

        when: "go to the create page"
        go "/project/${project.id}/testCase/create"

        then: "default text selected"
        def page = browser.page(CreateTestCasePage)
        page.areaSelect().selectedText == ""
        page.areaSelect().selected == ""
    }
}
