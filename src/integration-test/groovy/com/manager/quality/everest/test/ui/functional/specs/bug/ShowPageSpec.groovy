package com.manager.quality.everest.test.ui.functional.specs.bug

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.ui.support.pages.bug.CreateBugPage
import com.manager.quality.everest.test.ui.support.pages.bug.EditBugPage
import com.manager.quality.everest.test.ui.support.pages.bug.ListBugPage
import com.manager.quality.everest.test.ui.support.pages.bug.ShowBugPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ShowPageSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    void "status message displayed after bug created"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")

        when: "create a bug"
        def page = at CreateBugPage
        page.createFreeFormBug()

        then: "at show page with message displayed"
        def showPage = at ShowBugPage
        showPage.statusMessage.text() ==~ /Bug \d+ created/
    }

    void "edit link directs to edit view"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        and: "go to list page"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        when: "click the edit button"
        def page = browser.page(ShowBugPage)
        page.goToEdit()

        then: "at the edit page"
        at EditBugPage
    }

    void "delete edit buttons not displayed for Read Only user"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        when: "go to list page"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowBugPage)
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

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        when: "go to list page"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        then: "create delete edit test case buttons are not displayed"
        def page = browser.page(ShowBugPage)
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

    void "bug not deleted if alert is canceled"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        and: "go to list page"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        when: "click delete and cancel | verify message"
        def showPage = browser.page(ShowBugPage)
        assert withConfirm(false) { showPage.deleteLink.click() } == "Are you sure?"

        then: "at show bug page"
        at ShowBugPage
    }

    void "updated message displays after updating bug"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Bugs')

        and: "go to list page"
        def bugsPage = at ListBugPage
        bugsPage.listTable.clickCell('Name', 0)

        and: "go to edit"
        def showPage = browser.page(ShowBugPage)
        showPage.goToEdit()

        when: "edit a bug"
        def page = browser.page(EditBugPage)
        page.edit()

        then: "at show bug page with message displayed"
        showPage.statusMessage.text() ==~ /Bug \d+ updated/
    }

    void "blank value when related items are null"() {
        given:
        def person = personService.list(max:1).first()
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        def bug = bugService.save(new Bug(name: "Show view blank related items", project: project, person: person,
                status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when:
        def page = to ShowBugPage, project.id, bug.id

        then:
        page.areaValue.text() == ""
        page.platformValue.text() == ""
        page.environmentsList.text() == ""
    }

    void "name displays for related items"() {
        given:
        def person = personService.list(max:1).first()
        def platform = new Platform(name: "platform testing platform III")
        def area = new Area(name: "area testing area III")
        def env = new Environment(name: "env testing env III")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform],
                areas: [area], environments: [env]))
        def bug = bugService.save(new Bug(name: "Show view related items displays names", project: project, person: person,
                platform: platform, area: area, environments: [env], status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when:
        def page = to ShowBugPage, project.id, bug.id

        then:
        page.platformValue.text() == "platform testing platform III"
        page.areaValue.text() == "area testing area III"
        page.environmentsList.text() == "env testing env III"
    }
}
