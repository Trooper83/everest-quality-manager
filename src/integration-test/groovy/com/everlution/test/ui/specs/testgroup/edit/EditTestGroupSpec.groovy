package com.everlution.test.ui.specs.testgroup.edit

import com.everlution.ProjectService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.EditTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditTestGroupSpec extends GebSpec {

    ProjectService projectService
    TestGroupService testGroupService

    void "authorized users can edit test group"(String username, String password) {
        setup: "get instance"
        def id = testGroupService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/testGroup/edit/${id}"

        when: "edit the instance"
        def page = browser.page(EditTestGroupPage)
        page.edit()

        then: "at show page with message displayed"
        at ShowTestGroupPage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
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
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/testGroup/edit/${id}"

        when: "edit instance"
        def editPage = at EditTestGroupPage
        editPage.editTestGroup("edited name")

        then: "at show page with edited data"
        def showPage = at ShowTestGroupPage
        showPage.nameValue.text() == "edited name"
    }
}
