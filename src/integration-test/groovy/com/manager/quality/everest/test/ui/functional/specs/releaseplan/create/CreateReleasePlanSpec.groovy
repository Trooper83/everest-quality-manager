package com.manager.quality.everest.test.ui.functional.specs.releaseplan.create

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class CreateReleasePlanSpec extends GebSpec {

    ProjectService projectService

    @Shared Project project

    def setup() {
        project = projectService.list(max: 1).first()
    }

    void "authorized users can create plan"(String username, String password) {
        given: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        def page = to(CreateReleasePlanPage, project.id)

        when: "create a bug"
        page.createReleasePlan()

        then: "at show page"
        at ShowReleasePlanPage

        where:
        username                         | password
        Credentials.BASIC.email | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "all create form data persists"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to the create page & create instance"
        def page = to(CreateReleasePlanPage, project.id)
        def name = DataFactory.releasePlan().name
        def notes = DataFactory.releasePlan().notes
        page.createReleasePlan(name, "ToDo", "July 25, 2024", "July 31, 2024", notes)

        then: "at show page"
        def show = at ShowReleasePlanPage
        verifyAll {
            show.nameValue.text() == name
            show.statusValue.text() == "ToDo"
            show.plannedDateValue.text() == "July 25, 2024"
            show.releaseDateValue.text() == "July 31, 2024"
            show.notesValue.text() == notes
        }
    }
}
