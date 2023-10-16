package com.everlution.test.ui.functional.specs.testiteration

import com.everlution.*
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.everlution.test.ui.support.pages.testiteration.ExecuteTestIterationPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class ExecutePageSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCaseService testCaseService
    TestCycleService testCycleService

    @Shared Project project
    @Shared TestCycle testCycle

    def setup() {
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def proj = new Project(name: pd.name, code: pd.code, testGroups: [group])
        project = projectService.save(proj)
        def person = personService.list(max: 1).first()
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        testCycleService.addTestIterations(testCycle, [testCase])
    }

    void "test cycle link directs to test cycle"() {
        given: "setup data"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        go "/project/${project.id}/testCycle/show/${testCycle.id}"

        and:
        def showCycle = at ShowTestCyclePage
        showCycle.testsTable.clickCell("", 0)

        when:
        def execute = at ExecuteTestIterationPage
        execute.goToTestCycle()

        then:
        at ShowTestCyclePage
    }

    void "result has correct options"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        go "/project/${project.id}/testCycle/show/${testCycle.id}"

        when:
        def showCycle = at ShowTestCyclePage
        showCycle.testsTable.clickCell("", 0)

        then:
        def show = at ExecuteTestIterationPage
        show.resultOptions*.text() == ["ToDo", "Pass", "Fail"]
    }
}
