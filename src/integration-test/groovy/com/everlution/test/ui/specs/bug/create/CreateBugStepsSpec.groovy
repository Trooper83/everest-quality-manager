package com.everlution.test.ui.specs.bug.create

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateBugStepsSpec extends GebSpec {

    void "removed test steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        def page = to CreateBugPage

        when: "fill in create form"
        page.completeCreateForm()

        and: "add a new test step"
        page.stepsTable.addStep("should not persist", "should not persist")

        and: "remove row"
        page.stepsTable.removeRow(1)

        and: "submit form"
        page.createButton.click()

        then: "at show page"
        def showPage = at ShowBugPage
        showPage.stepsTable.getRowCount() == 1
    }
}
