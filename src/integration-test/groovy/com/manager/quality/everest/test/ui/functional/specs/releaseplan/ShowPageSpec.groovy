package com.manager.quality.everest.test.ui.functional.specs.releaseplan

import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.services.releaseplan.ReleasePlanService
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.domains.TestCycle
import com.manager.quality.everest.services.testcycle.TestCycleService
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.services.testgroup.TestGroupService
import com.manager.quality.everest.services.testiteration.TestIterationService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.EditReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.testcycle.ShowTestCyclePage
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
    TestGroupService testGroupService
    TestIterationService testIterationService

    void "status message displayed after plan created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Release Plan")

        when: "create a plan"
        browser.page(CreateReleasePlanPage).createReleasePlan()

        then: "at show page with message displayed"
        def showPage = at ShowReleasePlanPage
        showPage.statusMessage.text() ==~ /ReleasePlan \d+ created/
    }

    void "edit link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        and: "click first test group in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        when: "click the edit button"
        def page = browser.page(ShowReleasePlanPage)
        page.goToEdit()

        then: "at the edit page"
        at EditReleasePlanPage
    }

    void "delete edit add test cycle buttons not displayed for Read Only user"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        when: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        then: "create delete edit add test cycle buttons are not displayed"
        def page = browser.page(ShowReleasePlanPage)
        verifyAll {
            !page.deleteLink.displayed
            !page.editLink.displayed
            !page.addTestCycleButton.displayed
        }
    }

    void "delete edit add test cycle buttons displayed for authorized users"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        when: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        then: "create delete edit buttons are not displayed"
        def page = browser.page(ShowReleasePlanPage)
        verifyAll {
            page.deleteLink.displayed
            page.editLink.displayed
            page.addTestCycleButton.displayed
        }

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "plan not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        when: "click delete and cancel | verify message"
        def showPage = browser.page(ShowReleasePlanPage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show page"
        at ShowReleasePlanPage
    }

    void "updated message displays after updating plan"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        and:
        def showPage = at ShowReleasePlanPage
        showPage.goToEdit()

        when: "edit a plan"
        def page = browser.page(EditReleasePlanPage)
        page.edit()

        then: "at show page with message displayed"
        showPage.statusMessage.text() ==~ /ReleasePlan \d+ updated/
    }

    void "create message displayed after test cycle created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        when: "create test cycle"
        browser.page(ShowReleasePlanPage).createTestCycle("test cycle 999", "", "")

        then:
        def showPage = at ShowReleasePlanPage
        showPage.statusMessage.text() ==~ /TestCycle \d+ created/
        showPage.isTestCyclePresent("test cycle 999")
    }

    void "test cycle content displays when button clicked"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        when: "create test cycle"
        browser.page(ShowReleasePlanPage).createTestCycle()

        then: "content not displayed"
        def showPage = at ShowReleasePlanPage
        !showPage.testCyclesContent.first().isDisplayed()

        and: "expand test cycle and content is displayed"
        showPage.testCycleButtons.first().click()
        showPage.testCyclesContent.first().isDisplayed()
    }

    void "view test cycle goes to test cycle"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        and: "create test cycle"
        browser.page(ShowReleasePlanPage).createTestCycle()

        when: "click first test cycle"
        def showPage = at ShowReleasePlanPage
        showPage.goToTestCycle(0)

        then: "at show test cycle view"
        at ShowTestCyclePage
    }

    void "verify platform options"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        def p = personService.list(max:1).first()
        def pd = DataFactory.project()
        def pr = new Project(name: pd.name, code: pd.code, platforms:
                [new Platform(name: "Platypus Form"), new Platform(name: "Form Platypus")])
        projectService.save(pr)
        def r = DataFactory.createReleasePlan(p, pr)

        when:
        def page = to ShowReleasePlanPage, pr.id, r.id

        then: "correct options are populated"
        page.displayAddTestCycleModal()
        page.testCycleModalPlatformOptions*.text().containsAll("Select a Platform...", "Form Platypus", "Platypus Form")

        and: "default value is blank"
        page.testCycleModalPlatformSelect().selected == ""
    }

    void "environ defaults with select text"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        when: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        then:"default value is select text"
        def page = browser.page(ShowReleasePlanPage)
        page.displayAddTestCycleModal()
        page.testCycleModalEnvironSelect().selectedText == "Select an Environment..."
        page.testCycleModalEnvironSelect().selected == ""
    }

    void "page loads when platform and environ is null"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        def p = personService.list(max:1).first()
        def t = DataFactory.createTestCycle(p)

        expect:
        !t.releasePlan.project.platforms
        !t.releasePlan.project.environments

        and:
        def show = to ShowReleasePlanPage, t.releasePlan.project.id, t.releasePlan.id

        when:
        show.createTestCycle("New Test Cycle", "", "")

        then:
        at ShowReleasePlanPage
    }

    void "add tests modal resets data when cancelled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        and:
        def page = browser.page(ShowReleasePlanPage)
        page.displayAddTestCycleModal()
        page.testCycleModalNameInput << 'testing'

        expect:
        page.testCycleModalNameInput.value() == 'testing'

        when:
        page.closeTestCycleModal()
        page.displayAddTestCycleModal()

        then:
        page.testCycleModalNameInput.text() == ''
    }

    void "testcycle progress bar has correct values"() {
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
        def tc3 = DataFactory.testCase()
        def testCase = new TestCase(name: tc.name, project: project, person: person)
        def testCase1 = new TestCase(name: tc1.name, project: project, person: person)
        def testCase2 = new TestCase(name: tc2.name, project: project, person: person)
        def testCase3 = new TestCase(name: tc3.name, project: project, person: person)
        testCaseService.save(testCase)
        testCaseService.save(testCase1)
        testCaseService.save(testCase2)
        testCaseService.save(testCase3)
        testCycleService.addTestIterations(testCycle, [testCase, testCase1, testCase2, testCase3])
        def iter = testCycle.testIterations.first()
        iter.lastResult = 'PASSED'
        testIterationService.save(iter)
        def iteration = testCycle.testIterations[2]
        iteration.lastResult = 'FAILED'
        testIterationService.save(iteration)
        def iteration1 = testCycle.testIterations[3]
        iteration1.lastResult = 'SKIPPED'
        testIterationService.save(iteration1)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to plan"
        to (ShowReleasePlanPage, project.id, plan.id)

        then:
        def show = at ShowReleasePlanPage
        def progressBar = show.testCycleProgressBars
        progressBar[0].find("[aria-label='passed']").attr('aria-valuenow') == '1'
        progressBar[0].find("[aria-label='failed']").attr('aria-valuenow') == '1'
        progressBar[0].find("[aria-label='todo']").attr('aria-valuenow') == '1'
        progressBar[0].find("[aria-label='skipped']").attr('aria-valuenow') == '1'
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
        to (ShowReleasePlanPage, project.id, plan.id)

        then:
        def show = at ShowReleasePlanPage
        def progressBar = show.testCycleProgressBars
        progressBar[0].find("[aria-label='passed']").attr('aria-valuenow') == '0'
        progressBar[0].find("[aria-label='failed']").attr('aria-valuenow') == '0'
        progressBar[0].find("[aria-label='todo']").attr('aria-valuenow') == '0'
    }
}
