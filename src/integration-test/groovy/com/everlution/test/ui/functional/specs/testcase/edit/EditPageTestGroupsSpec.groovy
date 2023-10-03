package com.everlution.test.ui.functional.specs.testcase.edit

import com.everlution.*
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageTestGroupsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "test group select defaults with multiple selected test group"() {
        setup: "project & testCase instances with test groups"
        def gd = DataFactory.testGroup()
        def gd1 = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def group1 = new TestGroup(name: gd1.name)
        def pd = DataFactory.project()
        def p = new Project(name: pd.name, code: pd.code, testGroups: [group, group1])
        def project = projectService.save(p)
        def tcd = DataFactory.testCase()
        def tc = new TestCase(person: person, name: tcd.name, executionMethod: tcd.executionMethod,
                type: tcd.type, project: project, testGroups: [group, group1])
        def testCase = testCaseService.save(tc)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        then: "groups are selected"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.testGroupsSelect().selectedText.containsAll([group.name, group1.name])
    }

    void "test group select defaults no selection when no group set"() {
        setup: "project & testCase instances with groups"
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def tcd = DataFactory.testCase()
        def tc = new TestCase(person: person, name: tcd.name, executionMethod: tcd.executionMethod,
                type: tcd.type, project: project)
        def testCase = testCaseService.save(tc)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        then: "field defaults with no selection"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.testGroupsSelect().selected.empty
    }

    void "exception handled when validation error present and group set to null"() {
        given: "project & test case instances with areas"
        def group = new TestGroup(name: DataFactory.testGroup().name)
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, testGroups: [group]))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case II", project: project,
                person: person, executionMethod: "Automated", type: "UI", testGroups: [group]))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        and: "testCase.testGroups is set to null"
        def page = browser.page(EditTestCasePage)
        page.testGroupsSelect().selected = [""]

        expect: "set to null"
        page.testGroupsSelect().selectedText == ["No Test Group..."]

        when: "add empty steps"
        page.scrollToBottom()
        page.stepsTable.selectStepsTab('free-form')
        page.stepsTable.addStep("", "")

        and: "submit"
        page.editTestCase()

        then: "at the edit page"
        at EditTestCasePage
    }
}
