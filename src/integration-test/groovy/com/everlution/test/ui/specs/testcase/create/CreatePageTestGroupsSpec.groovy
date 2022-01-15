package com.everlution.test.ui.specs.testcase.create

import com.everlution.*
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class CreatePageTestGroupsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestGroupService testGroupService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        setup: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        person = personService.list(max: 1).first()
    }

    void "test groups field has no value set"() {
        given: "go to the create page"
        to CreateTestCasePage

        when: "project is selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = "1"

        then: "default text selected"
        page.testGroupsSelect().selectedText == []
        page.testGroupsSelect().selected == []
    }

    void "test groups field defaults with no group option"() {
        given: "go to the create page"
        to CreateTestCasePage

        when: "project is selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = "1"

        then: "default text present"
        page.testGroupsOptions[0].text() == "--No Test Group--"
    }

    void "test group field defaults disabled"() {
        given: "go to the create page"
        to CreateTestCasePage

        expect: "defaults disabled"
        def page = browser.page(CreateTestCasePage)
        page.testGroupsSelect().disabled
    }

    void "test groups field disabled and depopulated when project is set to default"() {
        given: "go to the create page"
        to CreateTestCasePage

        and: "project is selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = "1"

        expect: "field enabled and populated"
        waitFor() { //need to wait for transition
            !page.testGroupsSelect().disabled
        }

        when: "project set to default"
        page.projectSelect().selected = ""

        then: "field is disabled, depopulated and set to default"
        waitFor(2) { //need to wait for transition
            page.testGroupsSelect().disabled
            page.testGroupsOptions*.text() == ["--No Test Group--"]
            page.testGroupsSelect().selectedText == []
            page.testGroupsSelect().selected == []
        }
    }

    void "test group select populates with only elements within the associated project"() {
        setup: "project & testCase instances with test groups"
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def tcd = DataFactory.testCase()
        def gd = DataFactory.testGroup()
        def group = testGroupService.save(new TestGroup(name: gd.name, project: project))
        project.addToTestGroups(group)
        def tc = new TestCase(person: person, name: tcd.name, executionMethod: tcd.executionMethod,
                type: tcd.type, project: project, testGroups: [group])
        testCaseService.save(tc)

        and: "go to the create page"
        to CreateTestCasePage

        when: "select project"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = project.name

        then: "populates with project.testGroups"
        waitFor() {
            page.testGroupsSelect().enabled
        }
        page.testGroupsOptions*.text() == ["--No Test Group--", group.name]
    }
}
