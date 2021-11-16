package com.everlution.test.ui.specs.testcase.edit

import com.everlution.*
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageEnvironmentsSpec extends GebSpec {

    ProjectService projectService
    TestCaseService testCaseService

    void "environment select populates with only elements within the associated project"() {
        setup: "project & testCase instances with environments"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def tcd = DataFactory.testCase()
        def tc = new TestCase(creator: tcd.creator, name: tcd.name, executionMethod: tcd.executionMethod,
                type: tcd.type, project: project, environments: [env])
        def testCase = testCaseService.save(tc)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/testCase/edit/${testCase.id}"

        then: "environment populates with project.environments"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.environmentsOptions*.text() == ["--No Environment--", env.name]
    }

    void "environment select defaults with multiple selected environment"() {
        setup: "project & testCase instances with environments"
        def env = new Environment(DataFactory.environment())
        def env1 = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def p = new Project(name: pd.name, code: pd.code, environments: [env, env1])
        def project = projectService.save(p)
        def tcd = DataFactory.testCase()
        def tc = new TestCase(creator: tcd.creator, name: tcd.name, executionMethod: tcd.executionMethod,
                type: tcd.type, project: project, environments: [env, env1])
        def testCase = testCaseService.save(tc)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/testCase/edit/${testCase.id}"

        then: "bug.environment is selected"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.environmentsSelect().selectedText == [env.name, env1.name]
    }

    void "environment select defaults no selection when no environment set"() {
        setup: "project & testCase instances with environments"
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def tcd = DataFactory.testCase()
        def tc = new TestCase(creator: tcd.creator, name: tcd.name, executionMethod: tcd.executionMethod,
                type: tcd.type, project: project)
        def testCase = testCaseService.save(tc)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/testCase/edit/${testCase.id}"

        then: "environment defaults with no selection"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.environmentsSelect().selected.empty
    }
}
