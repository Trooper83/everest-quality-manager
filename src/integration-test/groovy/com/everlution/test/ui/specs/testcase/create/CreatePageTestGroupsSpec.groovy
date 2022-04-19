package com.everlution.test.ui.specs.testcase.create

import com.everlution.*
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class CreatePageTestGroupsSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

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
        when: "project is selected"
        def page = browser.page(CreateTestCasePage)

        then: "default text present"
        page.testGroupsOptions[0].text() == "--Select Test Groups--"
    }
}
