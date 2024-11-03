package com.manager.quality.everest.test.ui.functional.specs.project

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class DeleteProjectSpec extends GebSpec {

    ProjectService projectService

    int id

    def setup() {
        def project = new Project(name: "delete bug spec", code: "dps")
        id = projectService.save(project).id
    }

    void "authorized users can delete Project"(String username, String password) {
        given: "log in as authorized user"
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/project/show/${id}"

        when: "delete project"
        def page = browser.at(ShowProjectPage)
        page.deleteProject()

        then: "at list page"
        at ListProjectPage

        where:
        username                         | password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     |Credentials.APP_ADMIN.password
    }
}
