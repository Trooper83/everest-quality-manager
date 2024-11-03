package com.manager.quality.everest.test.ui.functional.specs.testiteration

import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.services.releaseplan.ReleasePlanService
import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.domains.TestCycle
import com.manager.quality.everest.services.testcycle.TestCycleService
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.manager.quality.everest.test.ui.support.pages.testiteration.ExecuteTestIterationPage
import com.manager.quality.everest.test.ui.support.pages.testiteration.ShowTestIterationPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

import java.text.SimpleDateFormat

@SendResults
@Integration
class ExecuteIterationSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCaseService testCaseService
    TestCycleService testCycleService

    void "update result persists and creates result"() {
        setup:
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def proj = new Project(name: pd.name, code: pd.code, testGroups: [group])
        def project = projectService.save(proj)
        def person = personService.list(max: 1).first()
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def cycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, cycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group],
                steps: [new Step(act: 'action')])
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

        when:
        def page = browser.page(ExecuteTestIterationPage)
        page.setResult("PASSED", "Some notes")

        then:
        def show = at ShowTestIterationPage
        show.statusMessage.text() ==~ /Test Iteration \S+ updated/
        show.resultsTable.getValueInColumn(0, "#") == "1"
        show.resultsTable.getValueInColumn(0, "Result") == "PASSED"
        show.resultsTable.getValueInColumn(0, "Notes") == "Some notes"
        show.resultsTable.getValueInColumn(0, "Executed By") == Credentials.BASIC.email

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy")
        Date date = new Date()
        show.resultsTable.getValueInColumn(0, "Date Executed").contains(formatter.format(date))
    }

    void "form fails to submit if result is blank"() {
        setup:
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def proj = new Project(name: pd.name, code: pd.code, testGroups: [group])
        def project = projectService.save(proj)
        def person = personService.list(max: 1).first()
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def cycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, cycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group],
                steps: [new Step(act: 'action')])
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

        when:
        def page = browser.page(ExecuteTestIterationPage)
        page.setResult("", "Some notes")

        then:
        at ExecuteTestIterationPage
    }
}
