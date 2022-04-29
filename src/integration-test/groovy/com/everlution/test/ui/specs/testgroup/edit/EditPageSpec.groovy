package com.everlution.test.ui.specs.testgroup.edit

import com.everlution.test.ui.support.data.Usernames
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
        loginPage.login(Usernames.BASIC.username, "password")
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToListsPage('Test Groups')
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)
        browser.page(ShowTestGroupPage).goToEdit()
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        def page = browser.page(EditTestGroupPage)
        page.getFields() == ["Project", "Name *"]
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        def page = browser.page(EditTestGroupPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }
}
