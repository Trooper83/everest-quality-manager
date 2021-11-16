package com.everlution.test.ui.specs.bug.edit

import com.everlution.BugService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageProjectSpec extends GebSpec {

    BugService bugService
    @Shared int id

    def setup() {
        id = bugService.list(max:1).first().id
    }

    void "project name tag is html span and project cannot be edited"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${id}"

        then: "project tag is span"
        EditBugPage page = browser.page(EditBugPage)
        page.projectNameField.tag() == "span"
    }
}
