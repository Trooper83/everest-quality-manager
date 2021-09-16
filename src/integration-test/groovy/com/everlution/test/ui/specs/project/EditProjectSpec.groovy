package com.everlution.test.ui.specs.project

import com.everlution.ProjectService
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditProjectSpec extends GebSpec {

    ProjectService projectService

    void "authorized users can edit test case"(String username, String password) {
        given: "create test case"
        def id = projectService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the test case"
        EditProjectPage page = browser.page(EditProjectPage)
        page.editProject()

        then: "at show page"
        at ShowProjectPage

        where:
        username        | password
        "project_admin" | "password"
        "org_admin"     | "password"
        "app_admin"     | "password"
    }
}
