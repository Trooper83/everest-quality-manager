package com.everlution.test.ui.specs.project

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateProjectSpec extends GebSpec {

    void "authorized users can create project"(String username, String password, String name, String code) {
        given: "login as a user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        to CreateProjectPage

        when: "create a project"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject(name, code)

        then: "at show page with message displayed"
        at ShowProjectPage

        where:
        username                         | password   | name                | code
        Usernames.PROJECT_ADMIN.username | "password" | "created project 1" | "CP1"
        Usernames.ORG_ADMIN.username     | "password" | "created project 2" | "CP2"
        Usernames.APP_ADMIN.username     | "password" | "created project 3" | "CP3"
    }
}
