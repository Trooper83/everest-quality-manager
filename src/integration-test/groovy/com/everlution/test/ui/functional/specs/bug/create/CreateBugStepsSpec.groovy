package com.everlution.test.ui.functional.specs.bug.create

import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateBugStepsSpec extends GebSpec {

    void "removed test steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")

        and: "go to the create page"
        def page = at CreateBugPage

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
