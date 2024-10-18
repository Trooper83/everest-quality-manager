package com.manager.quality.everest.test.ui.functional.specs.releaseplan

import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.services.releaseplan.ReleasePlanService
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class DeleteReleasePlanSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService

    @Shared Long id
    @Shared Project project

    def setup() {
        project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def plan = new ReleasePlan(name: "delete spec plan", project: project, status: "ToDo", person: person)
        id = releasePlanService.save(plan).id
    }

    void "authorized users can delete release plan"(String username, String password) {
        given: "log in as authorized user"
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        to(ShowReleasePlanPage, project.id, id)

        when: "delete instance"
        def showPage = browser.at(ShowReleasePlanPage)
        showPage.deletePlan()

        then: "at list page"
        at ListReleasePlanPage

        where:
        username                         | password
        Credentials.BASIC.email | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email  | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email      | Credentials.APP_ADMIN.password
    }
}
