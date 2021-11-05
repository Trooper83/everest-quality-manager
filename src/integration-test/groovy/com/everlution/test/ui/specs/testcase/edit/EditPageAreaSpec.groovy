package com.everlution.test.ui.specs.testcase.edit

import com.everlution.Area
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageAreaSpec extends GebSpec {

    ProjectService projectService
    TestCaseService testCaseService

    void "area select populates with only elements within the associated project"() {
        setup: "project & test case instances with areas"
        def area = new Area(name: "area testing area")
        def project = projectService.save(new Project(name: "area tc testing project", code: "ACP", areas: [area]))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case", project: project,
                creator: "testing", executionMethod: "Automated", type: "UI"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/testCase/edit/${testCase.id}"

        then: "area populates with project.areas"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.areaOptions*.text() == ["", area.name]
    }

    void "area select defaults with selected area"() {
        setup: "project & test case instances with areas"
        def area = new Area(name: "area testing area")
        def project = projectService.save(new Project(name: "area tc testing project I", code: "AP1", areas: [area]))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case I", project: project,
                creator: "testing", executionMethod: "Automated", type: "UI", area: area))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/testCase/edit/${testCase.id}"

        then: "testCase.area is selected"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.areaSelect().selectedText == area.name
    }

    void "area select defaults empty text when no area set"() {
        setup: "project & test case instances with areas"
        def project = projectService.save(new Project(name: "area tc testing project II", code: "AP2"))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case II", project: project,
                creator: "testing", executionMethod: "Automated", type: "UI"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/testCase/edit/${testCase.id}"

        then: "testCase.area is selected"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.areaSelect().selectedText == ""
    }
}
