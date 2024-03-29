package com.everlution.test.ui.functional.specs.releaseplan

import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.domains.ReleasePlan
import com.everlution.services.releaseplan.ReleasePlanService
import com.everlution.domains.TestCase
import com.everlution.services.testcase.TestCaseService
import com.everlution.domains.TestCycle
import com.everlution.services.testcycle.TestCycleService
import com.everlution.domains.TestGroup
import com.everlution.services.testgroup.TestGroupService
import com.everlution.services.testiteration.TestIterationService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.EditReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

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
        browser.page(ShowReleasePlanPage).createTestCycle("test cycle 999", "1", "Web")

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

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

        when: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        then: "correct options are populated"
        def page = browser.page(ShowReleasePlanPage)
        page.displayAddTestCycleModal()
        page.testCycleModalPlatformOptions*.text() == ["Select a Platform...", "Android", "iOS", "Web"]

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
        def testCase = new TestCase(name: tc.name, project: project, person: person)
        def testCase1 = new TestCase(name: tc1.name, project: project, person: person)
        def testCase2 = new TestCase(name: tc2.name, project: project, person: person)
        testCaseService.save(testCase)
        testCaseService.save(testCase1)
        testCaseService.save(testCase2)
        testCycleService.addTestIterations(testCycle, [testCase, testCase1, testCase2])
        def iter = testCycle.testIterations.first()
        iter.result = 'Passed'
        testIterationService.save(iter)
        def iteration = testCycle.testIterations[2]
        iteration.result = 'Failed'
        testIterationService.save(iteration)

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
