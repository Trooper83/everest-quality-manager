package com.manager.quality.everest.test.ui.functional.specs.testiteration

import com.manager.quality.everest.domains.TestIteration
import com.manager.quality.everest.domains.TestIterationResult
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.services.releaseplan.ReleasePlanService
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.domains.TestCycle
import com.manager.quality.everest.services.testcycle.TestCycleService
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.services.testiteration.TestIterationService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testcase.ShowTestCasePage
import com.manager.quality.everest.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.manager.quality.everest.test.ui.support.pages.testiteration.ShowTestIterationPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ShowPageSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCaseService testCaseService
    TestCycleService testCycleService
    TestIterationService testIterationService

    void "test case link directs to test case"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def person = personService.list(max: 1).first()
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        testCycleService.addTestIterations(testCycle, [testCase])

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        go "/project/${project.id}/testCycle/show/${testCycle.id}"

        and:
        def showCycle = at ShowTestCyclePage
        showCycle.testsTable.clickCell("Name", 0)

        when:
        def show = at ShowTestIterationPage
        show.goToTestCase()

        then:
        at ShowTestCasePage
    }

    void "test cycle link directs to test cycle"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def person = personService.list(max: 1).first()
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        go "/project/${project.id}/testCycle/show/${testCycle.id}"

        and:
        def showCycle = at ShowTestCyclePage
        showCycle.addTestsByGroup(group.name)

        and:
        showCycle.scrollToBottom()
        showCycle.testsTable.clickCell("Name", 0)

        when:
        def showIteration = at ShowTestIterationPage
        showIteration.goToTestCycle()

        then:
        at ShowTestCyclePage
    }

    void "result has the correct classes"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def person = personService.list(max: 1).first()
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        testCycleService.addTestIterations(testCycle, [testCase])
        TestIteration iter = testCycle.testIterations.first()
        iter.lastResult = 'PASSED'
        iter.lastExecutedBy = person
        iter.addToResults(new TestIterationResult(person: person, result: "PASSED"))
        iter.addToResults(new TestIterationResult(person: person, result: "FAILED"))
        iter.addToResults(new TestIterationResult(person: person, result: "SKIPPED"))
        testIterationService.save(iter)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        go "/project/${project.id}/testCycle/show/${testCycle.id}"

        and:
        def showCycle = at ShowTestCyclePage
        showCycle.addTestsByGroup(group.name)

        when:
        showCycle.scrollToBottom()
        showCycle.testsTable.clickCell("Name", 0)

        then:
        $("span", text: "PASSED").hasClass("text-bg-success")
        $("span", text: "SKIPPED").hasClass("text-bg-warning")
        $("span", text: "FAILED").hasClass("text-bg-danger")
    }

    void "results are numbered in descending order"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def person = personService.list(max: 1).first()
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        testCycleService.addTestIterations(testCycle, [testCase])
        TestIteration iter = testCycle.testIterations.first()
        iter.lastResult = 'PASSED'
        iter.lastExecutedBy = person
        iter.addToResults(new TestIterationResult(person: person, result: "PASSED"))
        iter.addToResults(new TestIterationResult(person: person, result: "FAILED"))
        iter.addToResults(new TestIterationResult(person: person, result: "SKIPPED"))
        testIterationService.save(iter)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        go "/project/${project.id}/testCycle/show/${testCycle.id}"

        and:
        def showCycle = at ShowTestCyclePage
        showCycle.addTestsByGroup(group.name)

        when:
        showCycle.scrollToBottom()
        showCycle.testsTable.clickCell("Name", 0)

        then:
        def showIter = at ShowTestIterationPage
        showIter.resultsTable.getValueInColumn(0, "#") == "3"
        showIter.resultsTable.getValueInColumn(1, "#") == "2"
        showIter.resultsTable.getValueInColumn(2, "#") == "1"
    }
}
