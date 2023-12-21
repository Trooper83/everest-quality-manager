package com.everlution.test.ui.functional.specs.stepTemplate

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.stepTemplate.CreateStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.EditStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ListStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ShowStepTemplatePage
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
        projectHomePage.sideBar.goToCreate("Step Template")

        when: "create a step"
        def page = at CreateStepTemplatePage
        page.createStepTemplate()

        then: "at show page with message displayed"
        def showPage = at ShowStepTemplatePage
        showPage.statusMessage.text() ==~ /StepTemplate \d+ created/
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
        projectHomePage.sideBar.goToProjectDomain('Step Templates')

        and: "go to list page"
        def stepsPage = at ListStepTemplatePage
        stepsPage.listTable.clickCell('Name', 0)

        when: "click the edit button"
        def page = browser.page(ShowStepTemplatePage)
        page.goToEdit()

        then: "at the edit page"
        at EditStepTemplatePage
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
        projectHomePage.sideBar.goToProjectDomain('Step Templates')

        when: "go to list page"
        def stepsPage = at ListStepTemplatePage
        stepsPage.listTable.clickCell('Name', 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowStepTemplatePage)
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
        projectHomePage.sideBar.goToProjectDomain('Step Templates')

        when: "go to list page"
        def stepsPage = at ListStepTemplatePage
        stepsPage.listTable.clickCell('Name', 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowStepTemplatePage)
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
        projectHomePage.sideBar.goToProjectDomain('Step Templates')

        and: "go to list page"
        def stepsPage = at ListStepTemplatePage
        stepsPage.listTable.clickCell('Name', 0)

        and: "go to edit"
        def showPage = browser.page(ShowStepTemplatePage)
        showPage.goToEdit()

        when: "edit a step"
        def page = browser.page(EditStepTemplatePage)
        page.edit()

        then: "at show step page with message displayed"
        showPage.statusMessage.text() ==~ /StepTemplate \d+ updated/
    }
}
