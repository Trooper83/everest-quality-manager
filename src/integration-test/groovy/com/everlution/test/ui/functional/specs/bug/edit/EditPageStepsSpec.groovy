package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageStepsSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    void "add test step row"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        and: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        expect: "row count is 1"
        def page = browser.page(EditBugPage)
        page.stepsTable.getStepsCount() == 1

        when: "add a test step row"
        page.stepsTable.addRowHotKey()

        then: "row count is 2"
        page.stepsTable.getStepsCount() == 2
    }

    void "remove test step row"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        when: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        and: "add a row"
        def page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addRow()

        then: "row count is 2"
        page.stepsTable.getStepsCount() == 2

        when: "remove the second row"
        page.stepsTable.removeRow(1)

        then: "row count is 1"
        page.stepsTable.getStepsCount() == 1
    }

    void "removing step adds hidden input"() {
        given: "valid bug with step"
        Project project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        Step step = new Step(act: "step123", result: "result123", person: person, project: project)
        Bug bug = new Bug(person: person,name: "first", description: "desc1",
                project: project, steps: [step], status: "Open", actual: "actual", expected: "expected")
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        expect:
        bug.steps.size() == 1

        when: "remove step"
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.removeRow(0)

        then: "hidden input added"
        page.stepRemovedInput.size() == 1
    }

    void "remove button only displayed for last step"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        and: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        and: "add a row"
        def page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addRow()

        expect:
        def count = page.stepsTable.getStepsCount() - 1
        page.stepsTable.getStep(count).find('input[value=Remove]').displayed

        when:
        page.stepsTable.addRow()

        then:
        !page.stepsTable.getStep(count).find('input[value=Remove]').displayed
        page.stepsTable.getStep(count + 1).find('input[value=Remove]').displayed
    }
}
