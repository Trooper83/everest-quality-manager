package com.everlution.test.ui.functional.specs.testcycle

import com.everlution.Person
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
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.everlution.test.ui.support.pages.testiteration.ExecuteTestIterationPage
import com.everlution.test.ui.support.pages.testiteration.ShowTestIterationPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class ShowPageSpec extends GebSpec {

    @Shared Person person
    @Shared TestCycle cycle

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCaseService testCaseService
    TestCycleService testCycleService

    def setup() {
        person = personService.list(max:1).first()
        cycle = DataFactory.createTestCycle(person)
    }

    void "add tests button not displayed for Read Only user"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to cycle"
        to (ShowTestCyclePage, cycle.releasePlan.project.id, cycle.id)

        expect:
        def show = at ShowTestCyclePage
        !show.addTestsButton.displayed
    }

    void "add tests button displayed for authorized users"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        to (ShowTestCyclePage, cycle.releasePlan.project.id, cycle.id)

        expect:
        def show = at ShowTestCyclePage
        show.addTestsButton.displayed

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "execute button not displayed for Read Only user"() {
        given:
        def gd = DataFactory.testGroup()
        def person = personService.list(max: 1).first()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        ArrayList<TestCase> tcList = [testCase]
        testCycleService.addTestIterations(testCycle, tcList)

        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to cycle"
        to (ShowTestCyclePage, cycle.releasePlan.project.id, testCycle.id)

        then:
        def show = at ShowTestCyclePage
        show.testsTable.rowCount > 0
        !show.testsTable.isValueInColumn("", "Execute")
    }

    void "execute button displayed for authorized users"(String username, String password) {
        given:
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def person = personService.list(max: 1).first()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        ArrayList<TestCase> tcList = [testCase]
        testCycleService.addTestIterations(testCycle, tcList)

        when:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to cycle"
        to (ShowTestCyclePage, cycle.releasePlan.project.id, testCycle.id)

        then:
        def show = at ShowTestCyclePage
        show.testsTable.rowCount > 0
        show.testsTable.isValueInColumn("", "Execute")

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "release plan link redirects to show release plan"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        to (ShowTestCyclePage, cycle.releasePlan.project.id, cycle.id)

        when:
        def show = at ShowTestCyclePage
        show.goToReleasePlan()

        then:
        at ShowReleasePlanPage
    }

    void "test iteration table id link opens show test iteration"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
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
        to (ShowTestCyclePage, project.id, testCycle.id)

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
        to (ShowTestCyclePage, project.id, testCycle.id)

        and:
        def show = at ShowTestCyclePage
        show.addTestsByGroup()

        when:
        show.testsTable.clickCell("", 0)

        then:
        at ExecuteTestIterationPage
    }

    void "add tests modal closes with x button"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        to (ShowTestCyclePage, cycle.releasePlan.project.id, cycle.id)

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

    void "add tests modal resets fields when cancelled"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
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
        to (ShowTestCyclePage, testCycle.releasePlan.project.id, testCycle.id)

        and:
        def show = at ShowTestCyclePage
        show.addTestsButton.click()
        waitFor {
            show.addTestsModal.displayed
        }
        show.testGroupsSelect().selected = [show.testGroupsOptions*.text().first()]

        expect:
        !show.testGroupsSelect().selected.empty

        when:
        show.cancelAddTestsModal()

        and:
        show.displayAddTestsModal()

        then:
        show.testGroupsSelect().selected.empty
    }


    void "correct test groups populate modal form"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
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
        to (ShowTestCyclePage, project.id, testCycle.id)

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
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        to (ShowTestCyclePage, cycle.releasePlan.project.id, cycle.id)

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
        to (ShowTestCyclePage, project.id, testCycle.id)

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
        to (ShowTestCyclePage, project.id, testCycle.id)

        when:
        def show = at ShowTestCyclePage
        show.addTestsByGroup(group.name)

        then:
        show.testsTable.getHeaders() == ["Id", "Name", "Result", "Executed By", ""]
    }

    void "success message displays when tests added"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
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
        to (ShowTestCyclePage, project.id, testCycle.id)

        when:
        def show = at ShowTestCyclePage
        show.addTestsByGroup()

        then:
        show.statusMessage.text() == "Tests successfully added"
    }

    void "pagination works for results"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)

        for (int i = 0; i <= 12; i++) {
            def td = DataFactory.testCase()
            def testCase = new TestCase(name: td.name, project: project, person: person, testGroups: [group])
            testCaseService.save(testCase)
        }

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to cycle"
        to (ShowTestCyclePage, project.id, testCycle.id)
        def show = at ShowTestCyclePage
        show.addTestsByGroup()

        when:
        show.scrollToBottom()
        def found = show.testsTable.getValueInColumn(0, 'Id')
        show.testsTable.goToPage('2')

        then:
        at ShowTestCyclePage
        !show.testsTable.isValueInColumn('Id', found)
    }
}
