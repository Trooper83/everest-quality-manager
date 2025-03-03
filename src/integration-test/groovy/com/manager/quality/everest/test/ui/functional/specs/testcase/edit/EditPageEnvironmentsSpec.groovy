package com.manager.quality.everest.test.ui.functional.specs.testcase.edit

import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class EditPageEnvironmentsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "environment select defaults with multiple selected environment"() {
        setup: "project & testCase instances with environments"
        def env = new Environment(DataFactory.environment())
        def env1 = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def p = new Project(name: pd.name, code: pd.code, environments: [env, env1])
        def project = projectService.save(p)
        def tcd = DataFactory.testCase()
        def tc = new TestCase(person: person, name: tcd.name, executionMethod: tcd.executionMethod,
                type: tcd.type, project: project, environments: [env, env1])
        def testCase = testCaseService.save(tc)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        then: "bug.environment is selected"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.environmentsSelect().selectedText.containsAll(env.name, env1.name)
    }

    void "environment select defaults no selection when no environment set"() {
        setup: "project & testCase instances with environments"
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

        then: "environment defaults with no selection"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.environmentsSelect().selected.empty
    }

    void "exception handled when validation error present and env set to null"() {
        given: "project & test case instances with areas"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case II", project: project,
                person: person, executionMethod: "Automated", type: "UI", environments: [env]))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        and: "testCase.env is set to null"
        def page = browser.page(EditTestCasePage)
        page.environmentsSelect().selected = [""]

        expect: "env set to null"
        page.environmentsSelect().selectedText == ["No Environment..."]

        when: "add empty steps"
        page.scrollToBottom()
        page.stepsTable.addStep("", "","")

        and: "submit"
        page.editTestCase()

        then: "at the edit page"
        at EditTestCasePage
    }

    void "options equal project areas"() {
        given: "project & test case instances with areas"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def testCase = testCaseService.save(new TestCase(name: "area testing test case II234", project: project,
                person: person, executionMethod: "Automated", type: "UI", environments: [env]))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditTestCasePage, project.id, testCase.id

        then:
        page.environmentsOptions*.text() == ["No Environment...", env.name]
    }
}
