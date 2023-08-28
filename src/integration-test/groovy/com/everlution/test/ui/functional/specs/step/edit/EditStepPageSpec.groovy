package com.everlution.test.ui.functional.specs.step.edit

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.step.EditStepPage
import com.everlution.test.ui.support.pages.step.ListStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditStepPageSpec extends GebSpec {

    void "error message displays on view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Steps')

        def stepsPage = at ListStepPage
        stepsPage.listTable.clickCell("Name", 0)
        def showPage = at ShowStepPage
        showPage.goToEdit()

        when:
        def editPage = browser.at(EditStepPage)
        editPage.editStep("action", "", "result")

        then:
        editPage.errorsMessage.displayed
    }

}
