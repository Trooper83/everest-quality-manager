package com.everlution.test.ui.specs.bug.create

import com.everlution.Area
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.github.javafaker.Faker
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateBugSpec extends GebSpec {

    ProjectService projectService

    void "authorized users can create bug"(String username, String password) {
        given: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create bug page"
        def page = to CreateBugPage

        when: "create a bug"
        page.createBug()

        then: "at show page"
        at ShowBugPage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "all create from data saved"() {
        setup: "get fake data"
        Faker faker = new Faker()
        def area = new Area(DataFactory.area())
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code, areas: [area]))
        def name = faker.zelda().game()
        def description = faker.zelda().character()
        def action = faker.lorem().sentence(5)
        def result = faker.lorem().sentence(7)

        when: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "create bug"
        CreateBugPage createPage = to CreateBugPage
        createPage.createBug(name, description, area.name, project.name, action, result)

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        verifyAll {
            showPage.areaValue.text() == area.name
            showPage.projectValue.text() == project.name
            showPage.nameValue.text() == name
            showPage.descriptionValue.text() == description
            showPage.stepsTable.isRowDisplayed(action, result)
        }
    }
}
