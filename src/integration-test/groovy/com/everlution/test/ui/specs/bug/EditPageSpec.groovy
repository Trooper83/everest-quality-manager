package com.everlution.test.ui.specs.bug

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageSpec extends GebSpec {

    BugService bugService
    ProjectService projectService
    @Shared int id

    def setup() {
        id = bugService.list(max:1).first().id
    }

    void "home link directs to home view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit"
        go "/bug/edit/${id}"

        when: "click the home button"
        def page = browser.page(EditBugPage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit"
        go "/bug/edit/${id}"

        when: "click the list link"
        def page = browser.page(EditBugPage)
        page.goToList()

        then: "at the list page"
        at ListBugPage
    }

    void "correct fields are displayed"() {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${id}"

        then: "correct fields are displayed"
        def page = browser.page(EditBugPage)
        page.getFields() == ["Description", "Name *", "Project *"]
    }

    void "required fields indicator displayed for required fields"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${id}"

        then: "required field indicators displayed"
        def page = browser.page(EditBugPage)
        page.areRequiredFieldIndicatorsDisplayed(["name"])
    }

    void "add test step row"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${id}"

        then: "row count is 1"
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

        when: "go to edit page"
        go "/bug/edit/${id}"

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
        Bug bug = new Bug(creator: "test",name: "first", description: "desc1",
                project: project, steps: [step])
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/bug/edit/${id}"

        expect:
        bug.steps.size() == 1

        when: "remove step"
        EditBugPage page = browser.page(EditBugPage)
        page.stepsTable.removeRow(0)

        then: "hidden input added"
        page.stepRemovedInput.size() == 1
    }
}
