package com.everlution.test.ui.specs.testcase.edit

import com.everlution.TestCaseService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageProjectSpec extends GebSpec {

    TestCaseService testCaseService
    @Shared int id

    def setup() {
        id = testCaseService.list(max:1).first().id
    }

    void "project name tag is html span and project cannot be edited"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/testCase/edit/${id}"

        then: "project tag is span"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.projectNameField.tag() == "span"
    }
}
