package com.everlution.test.ui.specs.project

import com.everlution.Area
import com.everlution.AreaService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditProjectSpec extends GebSpec {

    AreaService areaService
    ProjectService projectService

    void "authorized users can edit test case"(String username, String password) {
        given: "get a project"
        def id = projectService.list(max: 1).first().id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the project"
        EditProjectPage page = browser.page(EditProjectPage)
        page.editProject()

        then: "at show page"
        at ShowProjectPage

        where:
        username                         | password
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "area tag can be added to existing project"() {
        given: "get a project"
        def id = projectService.list(max: 1).first().id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the project"
        EditProjectPage page = browser.page(EditProjectPage)
        page.addAreaTag("Added area tag")
        page.editProject()

        then: "area tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isAreaDisplayed("Added area tag")
    }

    void "area tag can be edited on existing project"() {
        given: "save a project"
        def project = new Project(name: "edited project", code: "edn", areas: [new Area(name: "area name")])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to show page"
        go "/project/show/${id}"

        expect: "area tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isAreaDisplayed("area name")

        when: "edit the project"
        showPage.goToEdit()
        EditProjectPage page = browser.page(EditProjectPage)
        page.editAreaTag("area name", "edited area tag")
        page.editProject()

        then: "area tag is displayed"
        showPage.isAreaDisplayed("edited area tag")
        !showPage.isAreaDisplayed("area name")
    }

    void "area tag can be deleted from existing project"() {
        given: "save a project"
        def area = new Area(name: "area name")
        def project = new Project(name: "edited project155", code: "edz", areas: [area])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to show page"
        go "/project/show/${id}"

        expect: "area to be found"
        areaService.get(area.id) != null

        and: "area tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isAreaDisplayed("area name")

        when: "remove the area tag"
        showPage.goToEdit()
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeAreaTag("area name")
        page.editProject()

        then: "area tag is displayed and was deleted"
        !showPage.isAreaDisplayed("area name")
        areaService.get(area.id) == null
    }
}
