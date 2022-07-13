package com.everlution.test.ui.specs.testgroup.edit

import com.everlution.ProjectService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.test.support.DataFactory
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
class EditTestGroupSpec extends GebSpec {

    ProjectService projectService
    TestGroupService testGroupService

    void "authorized users can edit test group"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToProjectDomain('Test Groups')

        and: "click first test group in list"
        def listPage = browser.page(ListTestGroupPage)
        listPage.listTable.clickCell("Name", 0)

        and: "go to edit"
        browser.page(ShowTestGroupPage).goToEdit()

        when: "edit the instance"
        def page = browser.page(EditTestGroupPage)
        page.edit()

        then: "at show page with message displayed"
        at ShowTestGroupPage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all edit from data saved"() {
        setup: "setup data"
        def project = projectService.list(max: 1).first()
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name, project: project)
        def id = testGroupService.save(group).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        to(EditTestGroupPage, project.id, id)

        when: "edit instance"
        def editPage = at EditTestGroupPage
        editPage.editTestGroup("edited name")

        then: "at show page with edited data"
        def showPage = at ShowTestGroupPage
        showPage.nameValue.text() == "edited name"
    }
}
