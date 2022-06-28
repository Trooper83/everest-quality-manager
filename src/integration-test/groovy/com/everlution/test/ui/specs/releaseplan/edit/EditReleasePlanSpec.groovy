package com.everlution.test.ui.specs.releaseplan.edit

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.releaseplan.EditReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditReleasePlanSpec extends GebSpec {

    ProjectService projectService
    ReleasePlanService releasePlanService

    void "authorized users can edit plan"(String username, String password) {
        setup: "get instance"
        def id = releasePlanService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.projectNavButtons.goToListsPage('Release Plans')

        and: "click first row in list"
        def listPage = browser.page(ListReleasePlanPage)
        listPage.listTable.clickCell("Name", 0)

        and: "go to edit"
        browser.page(ShowReleasePlanPage).goToEdit()

        when: "edit the instance"
        def page = browser.page(EditReleasePlanPage)
        page.edit()

        then: "at show page with message displayed"
        at ShowReleasePlanPage

        where:
        username                         | password
        Credentials.BASIC.email          | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email  | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email      | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email      | Credentials.APP_ADMIN.password
    }

    void "all edit from data saved"() {
        setup: "setup data"
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code))
        def pd = DataFactory.releasePlan()
        def plan = new ReleasePlan(name: pd.name, project: project)
        def id = releasePlanService.save(plan).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        to(EditReleasePlanPage, project.id, id)

        when: "edit instance"
        def editPage = at EditReleasePlanPage
        editPage.editReleasePlan("edited name")

        then: "at show page with edited data"
        def showPage = at ShowReleasePlanPage
        showPage.nameValue.text() == "edited name"
    }
}
