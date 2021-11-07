package com.everlution.test.ui.specs.bug.edit

import com.everlution.Area
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
        page.getFields() == ["Project", "Area", "Description", "Name *"]
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

    void "area select populates with only elements within the associated project"() {
        setup: "project & bug instances with areas"
        def area = new Area(name: "area testing area")
        def project = projectService.save(new Project(name: "area testing project", code: "ATP", areas: [area]))
        def bug = bugService.save(new Bug(name: "area testing bug", project: project, creator: "testing"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${bug.id}"

        then: "area populates with project.areas"
        EditBugPage page = browser.page(EditBugPage)
        page.areaOptions*.text() == ["", area.name]
    }

    void "area select defaults with selected area"() {
        setup: "project & bug instances with areas"
        def area = new Area(name: "area testing area II")
        def project = projectService.save(new Project(name: "area testing project II", code: "ATI", areas: [area]))
        def bug = bugService.save(new Bug(name: "area testing bug II", project: project, creator: "testing", area: area))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${bug.id}"

        then: "bug.area is selected"
        EditBugPage page = browser.page(EditBugPage)
        page.areaSelect().selectedText == area.name
    }

    void "area select defaults empty text when no area set"() {
        setup: "project & bug instances with areas"
        def project = projectService.save(new Project(name: "area testing project III", code: "AT2"))
        def bug = bugService.save(new Bug(name: "area testing bug III", project: project, creator: "testing"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${bug.id}"

        then: "area defaults with no selection"
        EditBugPage page = browser.page(EditBugPage)
        page.areaSelect().selectedText == ""
    }

    void "project name tag is html span and project cannot be edited"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${id}"

        then: "project tag is span"
        EditBugPage page = browser.page(EditBugPage)
        page.projectNameField.tag() == "span"
    }
}