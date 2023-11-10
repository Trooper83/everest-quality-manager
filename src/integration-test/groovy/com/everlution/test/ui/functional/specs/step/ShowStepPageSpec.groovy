package com.everlution.test.ui.functional.specs.step

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.step.CreateStepPage
import com.everlution.test.ui.support.pages.step.EditStepPage
import com.everlution.test.ui.support.pages.step.ListStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ShowStepPageSpec extends GebSpec {

    void "status message displayed after step created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create step page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Step")

        when: "create a step"
        def page = at CreateStepPage
        page.createStep()

        then: "at show page with message displayed"
        def showPage = at ShowStepPage
        showPage.statusMessage.text() ==~ /Step \d+ created/
    }

    void "edit link directs to edit view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists step page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Steps')

        and: "go to list page"
        def stepsPage = at ListStepPage
        stepsPage.listTable.clickCell('Name', 0)

        when: "click the edit button"
        def page = browser.page(ShowStepPage)
        page.goToEdit()

        then: "at the edit page"
        at EditStepPage
    }

    void "delete edit buttons not displayed for Read Only user"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists step page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Steps')

        when: "go to list page"
        def stepsPage = at ListStepPage
        stepsPage.listTable.clickCell('Name', 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowStepPage)
        verifyAll {
            !page.deleteLink.displayed
            !page.editLink.displayed
        }
    }

    void "delete edit buttons displayed for authorized users"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists step page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Steps')

        when: "go to list page"
        def stepsPage = at ListStepPage
        stepsPage.listTable.clickCell('Name', 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowStepPage)
        verifyAll {
            page.deleteLink.displayed
            page.editLink.displayed
        }

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "updated message displays after updating step"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists step page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Steps')

        and: "go to list page"
        def stepsPage = at ListStepPage
        stepsPage.listTable.clickCell('Name', 0)

        and: "go to edit"
        def showPage = browser.page(ShowStepPage)
        showPage.goToEdit()

        when: "edit a step"
        def page = browser.page(EditStepPage)
        page.edit()

        then: "at show step page with message displayed"
        showPage.statusMessage.text() ==~ /Step \d+ updated/
    }
}
