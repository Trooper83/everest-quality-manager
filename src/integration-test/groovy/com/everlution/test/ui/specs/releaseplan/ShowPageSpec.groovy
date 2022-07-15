package com.everlution.test.ui.specs.releaseplan

import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Credentials
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

    ProjectService projectService

    void "status message displayed after plan created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToCreatePage('Release Plan')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

        when: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        then:"default value is select text"
        def page = browser.page(ShowReleasePlanPage)
        page.displayAddTestCycleModal()
        page.testCycleModalEnvironSelect().selectedText == "Select an Environment..."
        page.testCycleModalEnvironSelect().selected == ""
    }

    void "add tests modal closes with cancel button"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToProjectDomain('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        when:
        def page = browser.page(ShowReleasePlanPage)
        page.displayAddTestCycleModal()
        page.cancelTestCycleModal()

        then:
        !page.testCycleModal.displayed
    }

    void "add tests modal closes with x button"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToProjectDomain('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        when:
        def page = browser.page(ShowReleasePlanPage)
        page.displayAddTestCycleModal()
        page.closeTestCycleModal()

        then:
        !page.testCycleModal.displayed
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
        projectHomePage.navBar.goToProjectDomain('Release Plans')

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

    void "create button menu displays"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to show page"
        def id = projectService.list(max: 1).first().id
        def listPage = to(ListReleasePlanPage, id)
        listPage.listTable.clickCell("Name", 0)

        when:
        def showPage = browser.page(ShowReleasePlanPage)
        showPage.projectNavButtons.openCreateMenu()

        then:
        showPage.projectNavButtons.isCreateMenuOpen()
    }
}
