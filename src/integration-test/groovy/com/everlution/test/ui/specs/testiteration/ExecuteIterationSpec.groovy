package com.everlution.test.ui.specs.testiteration

import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestCycle
import com.everlution.TestCycleService
import com.everlution.TestGroup
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.everlution.test.ui.support.pages.testiteration.ExecuteTestIterationPage
import com.everlution.test.ui.support.pages.testiteration.ShowTestIterationPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ExecuteIterationSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCaseService testCaseService
    TestCycleService testCycleService

    void "update result persists"() {
        setup:
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def proj = new Project(name: pd.name, code: pd.code, testGroups: [group])
        def project = projectService.save(proj)
        def plan = new ReleasePlan(name: "release plan 1", project: project)
        releasePlanService.save(plan)
        def cycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, cycle)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        testCycleService.addTestIterations(cycle, [testCase])

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        go "/project/${project.id}/testCycle/show/${cycle.id}"

        and:
        def showCycle = at ShowTestCyclePage
        showCycle.testsTable.clickCell("", 0)

        expect:
        def page = browser.page(ExecuteTestIterationPage)
        page.resultSelect().selectedText == "ToDo"

        when:
        page.setResult("Pass", "Some notes")

        then:
        def show = at ShowTestIterationPage
        show.statusMessage.text() ==~ /Test Iteration \S+ updated/
        show.resultValue.text() == "Pass"
        show.notesValue.text() == "Some notes"
    }
}
