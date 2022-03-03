package com.everlution.test.ui.specs.testcycle

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
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.everlution.test.ui.support.pages.testiteration.ExecuteTestIterationPage
import com.everlution.test.ui.support.pages.testiteration.ShowTestIterationPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class ShowPageSpec extends GebSpec {

    @Shared TestCycle cycle

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCaseService testCaseService
    TestCycleService testCycleService

    def setup() {
        cycle = DataFactory.getTestCycle()
    }

    void "back to release plan link goes to release plan"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${cycle.id}?releasePlan.id=${cycle.releasePlan.id}"

        when: "go back to plan"
        def show = at(ShowTestCyclePage)
        show.goBackToPlan()

        then: "at release plan view"
        at ShowReleasePlanPage
    }

    void "correct fields are displayed"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${cycle.id}?releasePlan.id=${cycle.releasePlan.id}"

        expect:
        def show = at ShowTestCyclePage
        show.getFields() == ["Release Plan", "Name", "Environment", "Platform"]
    }

    void "add tests button not displayed for Read Only user"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${cycle.id}?releasePlan.id=${cycle.releasePlan.id}"

        expect:
        def show = at ShowTestCyclePage
        !show.addTestsButton.displayed
    }

    void "add tests button displayed for authorized users"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${cycle.id}?releasePlan.id=${cycle.releasePlan.id}"

        expect:
        def show = at ShowTestCyclePage
        show.addTestsButton.displayed

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "test iteration table id link opens show test iteration"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        testCycleService.save(testCycle)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${testCycle.id}?releasePlan.id=${plan.id}"

        and:
        def show = at ShowTestCyclePage
        show.addTestsByGroup()

        when:
        show.testsTable.clickCell("Id", 0)

        then:
        at ShowTestIterationPage
    }

    void "test iteration table execute link opens execute test iteration"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        testCycleService.save(testCycle)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${testCycle.id}?releasePlan.id=${plan.id}"

        and:
        def show = at ShowTestCyclePage
        show.addTestsByGroup()

        when:
        show.testsTable.clickCell("", 0)

        then:
        at ExecuteTestIterationPage
    }

    void "add tests modal closes with cancel button"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${cycle.id}?releasePlan.id=${cycle.releasePlan.id}"

        and:
        def show = at ShowTestCyclePage
        show.addTestsButton.click()
        waitFor {
            show.addTestsModal.displayed
        }

        when:
        show.closeAddTestsModal()

        then:
        waitFor {
            !show.addTestsModal.displayed
        }
    }

    void "add tests modal closes with x button"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${cycle.id}?releasePlan.id=${cycle.releasePlan.id}"

        and:
        def show = at ShowTestCyclePage
        show.addTestsButton.click()
        waitFor {
            show.addTestsModal.displayed
        }

        when:
        show.cancelAddTestsModal()

        then:
        waitFor {
            !show.addTestsModal.displayed
        }
    }

    void "correct test groups populate modal form"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        testCycleService.save(testCycle)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${testCycle.id}?releasePlan.id=${plan.id}"

        when:
        def show = at ShowTestCyclePage
        show.displayAddTestsModal()

        then:
        show.testGroupsOptions*.text() == project.testGroups*.name
    }

    void "submitting add tests form with no tests displays message"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${cycle.id}?releasePlan.id=${cycle.releasePlan.id}"

        and:
        def show = at ShowTestCyclePage
        show.addTestsButton.click()
        waitFor {
            show.addTestsModal.displayed
        }

        when:
        show.addTestsModalSubmitButton.click()

        then:
        show.statusMessage.text() == "No tests added"
    }

    void "added tests display in tests table"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        testCycleService.save(testCycle)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${testCycle.id}?releasePlan.id=${cycle.releasePlan.id}"

        when:
        def show = at ShowTestCyclePage
        show.addTestsByGroup()

        then:
        show.testsTable.getRowCount() == 1
    }

    void "verify test table headers"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        testCycleService.save(testCycle)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${testCycle.id}?releasePlan.id=${plan.id}"

        when:
        def show = at ShowTestCyclePage
        show.addTestsByGroup(group.name)

        then:
        show.testsTable.getHeaders() == ["Id", "Name", "Result", ""]
    }

    void "success message displays when tests added"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        testCycleService.save(testCycle)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to cycle"
        go "/testCycle/show/${testCycle.id}?releasePlan.id=${plan.id}"

        when:
        def show = at ShowTestCyclePage
        show.addTestsByGroup()

        then:
        show.statusMessage.text() == "Tests successfully added"
    }
}
