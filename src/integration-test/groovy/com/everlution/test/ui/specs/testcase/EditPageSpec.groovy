package com.everlution.test.ui.specs.testcase

import com.everlution.test.ui.support.pages.EditTestCasePage
import com.everlution.test.ui.support.pages.LoginPage
import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

@Integration
@Rollback
class EditPageSpec extends GebSpec {

    void "edit view displays unauthorized error for read_only user"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login("read_only", "password")

        when: "go to edit test case page"
        go "/testCase/edit"

        then: "unauthorized message is displayed"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.errorText.text() == "Sorry, you're not authorized to view this page."
    }
}
