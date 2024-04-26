package com.everlution.test.ui.functional.specs.testiteration

import com.everlution.domains.TestIteration
import com.everlution.domains.TestIterationResult
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.domains.ReleasePlan
import com.everlution.services.releaseplan.ReleasePlanService
import com.everlution.domains.TestCase
import com.everlution.services.testcase.TestCaseService
import com.everlution.domains.TestCycle
import com.everlution.services.testcycle.TestCycleService
import com.everlution.domains.TestGroup
import com.everlution.services.testiteration.TestIterationService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.everlution.test.ui.support.pages.testiteration.ShowTestIterationPage
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
        def tc1 = DataFactory.testCase()
        def tc2 = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        def testCase1 = new TestCase(name: tc1.name, project: project, person: person, testGroups: [group])
        def testCase2 = new TestCase(name: tc2.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        testCaseService.save(testCase1)
        testCaseService.save(testCase2)
        testCycleService.addTestIterations(testCycle, [testCase, testCase1, testCase2])
        TestIteration iter = testCycle.testIterations.first()
        iter.lastResult = 'PASSED'
        iter.lastExecutedBy = person
        iter.addToResults(new TestIterationResult(person: person, result: "PASSED"))
        testIterationService.save(iter)
        TestIteration iter1 = testCycle.testIterations.first()
        iter1.lastResult = 'FAILED'
        iter1.lastExecutedBy = person
        iter1.addToResults(new TestIterationResult(person: person, result: "FAILED"))
        testIterationService.save(iter)
        TestIteration iter2 = testCycle.testIterations.first()
        iter2.lastResult = 'SKIPPED'
        iter2.lastExecutedBy = person
        iter2.addToResults(new TestIterationResult(person: person, result: "SKIPPED"))
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

}
