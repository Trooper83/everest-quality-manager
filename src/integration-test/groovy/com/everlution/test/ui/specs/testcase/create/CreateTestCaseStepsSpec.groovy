package com.everlution.test.ui.specs.testcase.create

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateTestCaseStepsSpec extends GebSpec {

    ProjectService projectService

    void "removed test steps are not saved"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/testCase/create"

        when: "fill in create form"
        def page = browser.page(CreateTestCasePage)
        page.completeCreateForm()

        and: "add a new test step"
        page.testStepTable.addStep("should not persist", "should not persist")

        and: "remove row"
        page.testStepTable.removeRow(1)

        and: "submit form"
        page.createButton.click()

        then: "at show page"
        at ShowTestCasePage
        def showPage = browser.page(ShowTestCasePage)
        showPage.testStepTable.getRowCount() == 1
    }
}
