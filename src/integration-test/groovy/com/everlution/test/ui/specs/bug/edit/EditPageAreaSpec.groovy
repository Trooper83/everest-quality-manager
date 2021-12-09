package com.everlution.test.ui.specs.bug.edit

import com.everlution.Area
import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageAreaSpec extends GebSpec {

    BugService bugService
    ProjectService projectService

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

    void "exception handled when validation error present and area set to null"() {
        given: "project & bug instances with areas"
        def area = new Area(name: "area testing area II")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def bug = bugService.save(new Bug(name: "area testing bug II", project: project, creator: "testing", area: area))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/bug/edit/${bug.id}"

        and: "bug.area is set to null"
        EditBugPage page = browser.page(EditBugPage)
        page.areaSelect().selected = ""

        expect: "area set to null"
        page.areaSelect().selectedText == ""

        when: "add empty steps"
        page.stepsTable.addStep("", "")

        and: "submit"
        page.editBug()

        then: "at the edit page"
        at EditBugPage
    }
}
