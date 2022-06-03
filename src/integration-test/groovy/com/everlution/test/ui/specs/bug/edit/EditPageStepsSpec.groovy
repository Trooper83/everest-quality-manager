package com.everlution.test.ui.specs.bug.edit

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageStepsSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService
    @Shared int id

    def setup() {
        id = bugService.list(max:1).first().id
    }

    void "add test step row"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToListsPage('Bugs')

        and: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.bugTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        expect: "row count is 1"
        def page = browser.page(EditBugPage)
        page.stepsTable.getRowCount() == 1

        when: "add a test step row"
        page.stepsTable.addRow()

        then: "row count is 2"
        page.stepsTable.getRowCount() == 2
    }

    void "remove test step row"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToListsPage('Bugs')

        when: "edit bug"
        def bugsPage = at ListBugPage
        bugsPage.bugTable.clickCell("Name", 0)
        def showPage = at ShowBugPage
        showPage.goToEdit()

        and: "add a row"
        def page = browser.page(EditBugPage)
        page.stepsTable.addRow()

        then: "row count is 2"
        page.stepsTable.getRowCount() == 2

        when: "remove the second row"
        page.stepsTable.removeRow(1)

        then: "row count is 1"
        page.stepsTable.getRowCount() == 1
    }

    void "removing step adds hidden input"() {
        given: "valid bug with step"
        Project project = projectService.list(max: 1).first()
        Step step = new Step(action: "step123", result: "result123")
        def person = personService.list(max: 1).first()
        Bug bug = new Bug(person: person,name: "first", description: "desc1",
                project: project, steps: [step], status: "Open")
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        expect:
        bug.steps.size() == 1

        when: "remove step"
        EditBugPage page = browser.page(EditBugPage)
        page.stepsTable.removeRow(0)

        then: "hidden input added"
        page.stepRemovedInput.size() == 1
    }
}
