package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    void "verify platform options"() {
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

        then: "correct options are populated"
        EditBugPage page = browser.page(EditBugPage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }
    }

    void "verify status options"() {
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

        then: "correct options are populated"
        EditBugPage page = browser.page(EditBugPage)
        verifyAll {
            page.statusOptions*.text() == ["Open", "Fixed", "Closed"]
        }
    }

    void "default steps panel displayed for bug with no steps"() {
        given: "create bug"
        Project project = projectService.list(max: 1).first()
        Person person = personService.list(max: 1).first()
        Bug bug = new Bug(person: person, name: "first1", project: project, status: 'Open')
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        then:
        def page = at EditBugPage
        page.stepsTable.builderTab.displayed
        page.stepsTable.freeFormTab.displayed
    }
}
