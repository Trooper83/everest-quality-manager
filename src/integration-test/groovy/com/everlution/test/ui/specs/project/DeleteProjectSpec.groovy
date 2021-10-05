package com.everlution.test.ui.specs.project

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

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
        def showPage = browser.at(ShowProjectPage)
        showPage.deleteProject()

        then: "at list page"
        at ListProjectPage

        where:
        username                         | password
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }
}
