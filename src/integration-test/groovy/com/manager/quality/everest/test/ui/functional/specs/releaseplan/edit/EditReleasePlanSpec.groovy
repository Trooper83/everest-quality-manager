package com.manager.quality.everest.test.ui.functional.specs.releaseplan.edit

import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.services.releaseplan.ReleasePlanService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.EditReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class EditReleasePlanSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService

    void "authorized users can edit plan"(String username, String password) {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the lists page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Release Plans')

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
        Credentials.BASIC.email | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email  | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email      | Credentials.APP_ADMIN.password
    }

    void "all edit from data saved"() {
        setup: "setup data"
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code))
        def pd = DataFactory.releasePlan()
        def person = personService.list(max:1).first()
        def plan = new ReleasePlan(name: pd.name, project: project, status: "ToDo", person: person)
        def id = releasePlanService.save(plan).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        to(EditReleasePlanPage, project.id, id)

        when: "edit instance"
        def editPage = at EditReleasePlanPage
        editPage.editReleasePlan("edited name", "Released", "August 10, 2024", "August 23, 2024", "edited notes")

        then: "at show page with edited data"
        def showPage = at ShowReleasePlanPage
        showPage.nameValue.text() == "edited name"
        showPage.notesValue.text() == "edited notes"
        showPage.statusValue.text() == "Released"
        showPage.plannedDateValue.text() == "August 10, 2024"
        showPage.releaseDateValue.text() == "August 23, 2024"
    }
}
