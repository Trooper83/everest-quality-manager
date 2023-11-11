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
import com.everlution.TestGroupService
import com.everlution.TestIterationService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
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
    TestGroupService testGroupService
    TestIterationService testIterationService
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
        show.addTestsByGroup(group.name)

        when:
        show.scrollToBottom()
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
        show.addTestsByGroup(group.name)

        when:
        show.scrollToBottom()
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
        show.testsTable.getHeaders() == ["Id", "Name", "Result", "Executed By", "Executed Date", ""]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
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
        def show = at ShowTestCyclePage
        show.addTestsByGroup(group.name)
        def page = browser.page(ListTestGroupPage)
        page.listTable.sortColumn(column)

        expect: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=asc')

        when:
        page.listTable.sortColumn(column)

        then: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=desc')

        where:
        column | propName
        'Id' | 'id'
        'Name' | 'name'
        'Result' | 'result'
        'Executed By' | 'person'
        'Executed Date' | 'dateExecuted'
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

        for (int i = 0; i <= 26; i++) {
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

    void "pagination params remain set with sorting"() {
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

        for (int i = 0; i <= 26; i++) {
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
        show.scrollToBottom()
        show.testsTable.goToPage('2')

        when:
        show.testsTable.sortColumn('Id')

        then:
        currentUrl.contains('offset=25')
        currentUrl.contains('max=25')
    }

    void "sort params remain set with pagination"() {
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

        for (int i = 0; i <= 26; i++) {
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
        show.addTestsByGroup(group.name)
        show.testsTable.sortColumn('Id')

        when:
        show.scrollToBottom()
        show.testsTable.goToPage('2')

        then:
        currentUrl.contains('sort=id')
        currentUrl.contains('order=asc')
    }

    void "progress bar has correct values"() {
        given: "setup data"
        def person = personService.list(max:1).first()
        def gd = DataFactory.testGroup()
        def project = projectService.list(max:1).first()
        def group = new TestGroup(name: gd.name, project: project)
        testGroupService.save(group)
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def tc1 = DataFactory.testCase()
        def tc2 = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person)
        def testCase1 = new TestCase(name: tc1.name, project: project, person: person)
        def testCase2 = new TestCase(name: tc2.name, project: project, person: person)
        testCaseService.save(testCase)
        testCaseService.save(testCase1)
        testCaseService.save(testCase2)
        testCycleService.addTestIterations(testCycle, [testCase, testCase1, testCase2])
        def iter = testCycle.testIterations.first()
        iter.result = 'Pass'
        testIterationService.save(iter)
        def iteration = testCycle.testIterations[2]
        iteration.result = 'Fail'
        testIterationService.save(iteration)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to plan"
        to (ShowTestCyclePage, project.id, testCycle.id)

        then:
        def show = at ShowTestCyclePage
        def progressBar = show.progressBar
        progressBar[0].find("[aria-label='passed']").attr('aria-valuenow') == '1'
        progressBar[0].find("[aria-label='failed']").attr('aria-valuenow') == '1'
        progressBar[0].find("[aria-label='todo']").attr('aria-valuenow') == '1'
    }

    void "empty progress bar displayed when no test iterations are present on cycle"() {
        given: "setup data"
        def person = personService.list(max:1).first()
        def gd = DataFactory.testGroup()
        def project = projectService.list(max:1).first()
        def group = new TestGroup(name: gd.name, project: project)
        testGroupService.save(group)
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to plan"
        to (ShowTestCyclePage, project.id, testCycle.id)

        then:
        def show = at ShowTestCyclePage
        def progressBar = show.progressBar
        progressBar[0].find("[aria-label='passed']").attr('aria-valuenow') == '0'
        progressBar[0].find("[aria-label='failed']").attr('aria-valuenow') == '0'
        progressBar[0].find("[aria-label='todo']").attr('aria-valuenow') == '0'
    }
}
