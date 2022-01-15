package com.everlution.test.ui.specs.testgroup.edit

import com.everlution.TestGroupService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.EditTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageProjectSpec extends GebSpec {

    @Shared int id

    TestGroupService testGroupService

    def setup() {
        id = testGroupService.list(max:1).first().id
    }

    void "project name tag is html span and project cannot be edited"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/testGroup/edit/${id}"

        then: "project tag is span"
        def page = browser.page(EditTestGroupPage)
        page.projectNameField.tag() == "span"
    }
}
