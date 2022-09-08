package com.everlution.test.ui.functional.specs.testgroup.edit

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testgroup.EditTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Groups')
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)
        browser.page(ShowTestGroupPage).goToEdit()
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(EditTestGroupPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }
}
