package com.everlution.test.ui.specs.testcase.create

import com.everlution.Area
import com.everlution.Environment
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestGroup
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateTestCaseSpec extends GebSpec {

    ProjectService projectService

    void "authorized users can create test case"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create test case page"
        to CreateTestCasePage

        when: "create a test case"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.createTestCase()

        then: "at show test case page"
        at ShowTestCasePage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "all create form data saved"() {
        setup: "get fake data"
        def area = new Area(DataFactory.area())
        def ed = DataFactory.environment()
        def ed1 = DataFactory.environment()
        def env = new Environment(ed)
        def env1 = new Environment(ed1)
        def gd = DataFactory.testGroup()
        def gd1 = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def group1 = new TestGroup(name: gd.name)
        def projectData = DataFactory.project()
        def project = projectService.save(
                new Project(name: projectData.name, code: projectData.code, areas: [area], environments: [env, env1],
                testGroups: [gd, gd1])
        )

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "create test case"
        CreateTestCasePage createPage = to CreateTestCasePage
        def tcd = DataFactory.testCase()
        createPage.createTestCase(
                tcd.name, tcd.description, project.name, area.name, [env.name, env1.name], [group.name, group1.name],
                "Automated", "UI", "Web")

        then: "data is displayed on show page"
        ShowTestCasePage showPage = at ShowTestCasePage
        verifyAll {
            showPage.areaValue.text() == area.name
            showPage.projectValue.text() == project.name
            showPage.nameValue.text() == tcd.name
            showPage.descriptionValue.text() == tcd.description
            showPage.executionMethodValue.text() == "Automated"
            showPage.typeValue.text() == "UI"
            showPage.platformValue.text() == "Web"
            showPage.areEnvironmentsDisplayed([env.name, env1.name])
            showPage.areTestGroupsDisplayed([group.name, group1.name])
        }
    }
}
