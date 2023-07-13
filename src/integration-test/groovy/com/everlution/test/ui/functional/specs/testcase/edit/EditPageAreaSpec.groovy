package com.everlution.test.ui.functional.specs.testcase.edit

import com.everlution.Area
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageAreaSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "area select defaults with selected area"() {
        setup: "project & test case instances with areas"
        def area = new Area(name: "area testing area")
        def project = projectService.save(new Project(name: "area tc testing project I", code: "AP1", areas: [area]))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case I", project: project,
                person: person, executionMethod: "Automated", type: "UI", area: area))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        then: "testCase.area is selected"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.areaSelect().selectedText == area.name
    }

    void "area select defaults empty text when no area set"() {
        setup: "project & test case instances with areas"
        def project = projectService.save(new Project(name: "area tc testing project II", code: "AP2"))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case II", project: project,
                person: person, executionMethod: "Automated", type: "UI"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        then: "testCase.area is selected"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.areaSelect().selectedText == ""
    }

    void "exception handled when validation error present and area set to null"() {
        given: "project & test case instances with areas"
        def area = new Area(name: "area testing area II")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case II", project: project,
                person: person, executionMethod: "Automated", type: "UI", area: area))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        and: "testCase.area is set to null"
        def page = browser.page(EditTestCasePage)
        page.areaSelect().selected = ""

        expect: "area set to null"
        page.areaSelect().selectedText == ""

        when: "add empty steps"
        page.scrollToBottom()
        page.stepsTable.addStep("", "")

        and: "submit"
        page.editTestCase()

        then: "at the edit page"
        at EditTestCasePage
    }
}
