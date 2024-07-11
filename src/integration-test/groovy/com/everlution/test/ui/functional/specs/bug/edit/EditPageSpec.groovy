package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.domains.Bug
import com.everlution.domains.Platform
import com.everlution.services.bug.BugService
import com.everlution.domains.Person
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class EditPageSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    void "verify platform options"() {
        given: "get fake data"
        def pl = new Platform(name: "Testing 123")
        def pl1 = new Platform(name: "Testing 321")
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code, platforms: [pl, pl1]))
        Person person = personService.list(max: 1).first()
        Bug bug = new Bug(person: person, name: "first1", project: project, status: 'Open')
        def id = bugService.save(bug).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        go "/project/${project.id}/bug/edit/${id}"

        then: "correct options are populated"
        def page = browser.page(EditBugPage)
        verifyAll {
            page.platformOptions*.text().containsAll(["Testing 123", "Testing 321"])
            page.platformOptions*.text().size() == 3
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
}
