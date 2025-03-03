package com.manager.quality.everest.test.ui.functional.specs.testcase.create

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testcase.CreateTestCasePage
import com.manager.quality.everest.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreateTestCaseSpec extends GebSpec {

    ProjectService projectService

    void "authorized users can create test case"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/testCase/create"

        when: "create a test case"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.createFreeFormTestCase()

        then: "at show test case page"
        at ShowTestCasePage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all create form data saved"() {
        given: "get fake data"
        def area = new Area(DataFactory.area())
        def ed = DataFactory.environment()
        def ed1 = DataFactory.environment()
        def env = new Environment(ed)
        def env1 = new Environment(ed1)
        def pl = new Platform(DataFactory.area())
        def gd = DataFactory.testGroup()
        def gd1 = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def group1 = new TestGroup(name: gd.name)
        def projectData = DataFactory.project()
        def project = projectService.save(
                new Project(name: projectData.name, code: projectData.code, areas: [area], environments: [env, env1],
                testGroups: [gd, gd1], platforms: [pl])
        )

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)


        when: "create test case"
        go "/project/${project.id}/testCase/create"
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        def tcd = DataFactory.testCase()
        createPage.createFreeFormTestCase(
                tcd.name, tcd.description, area.name, [env.name, env1.name], [group.name, group1.name],
                "Automated", "UI", pl.name, [], tcd.verify)

        then: "data is displayed on show page"
        ShowTestCasePage showPage = at ShowTestCasePage
        verifyAll {
            showPage.areaValue.text() == area.name
            showPage.nameValue.text() == tcd.name
            showPage.descriptionValue.text() == tcd.description
            showPage.executionMethodValue.text() == "Automated"
            showPage.typeValue.text() == "UI"
            showPage.platformValue.text() == pl.name
            showPage.areEnvironmentsDisplayed([env.name, env1.name])
            showPage.areTestGroupsDisplayed([group.name, group1.name])
            showPage.verifyValue.text() == tcd.verify
        }
    }
}
