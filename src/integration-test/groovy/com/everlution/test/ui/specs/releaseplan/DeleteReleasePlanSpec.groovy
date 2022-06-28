package com.everlution.test.ui.specs.releaseplan

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class DeleteReleasePlanSpec extends GebSpec {

    ProjectService projectService
    ReleasePlanService releasePlanService

    @Shared Long id
    @Shared Project project

    def setup() {
        project = projectService.list(max: 1).first()
        def plan = new ReleasePlan(name: "delete spec plan", project: project)
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
        Credentials.BASIC.email          | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email  | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email      | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email      | Credentials.APP_ADMIN.password
    }
}
