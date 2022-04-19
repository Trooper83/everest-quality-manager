package com.everlution.test.ui.specs.bug.create

import com.everlution.Area
import com.everlution.Environment
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
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

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToCreatePage('Bug')

        when: "create a bug"
        def page = at CreateBugPage
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
        def ed = DataFactory.environment()
        def ed1 = DataFactory.environment()
        def env = new Environment(ed)
        def env1 = new Environment(ed1)
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code,
                areas: [area], environments: [env, env1]))
        def name = faker.zelda().game()
        def description = faker.zelda().character()
        def action = faker.lorem().sentence(5)
        def result = faker.lorem().sentence(7)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', project.name)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToCreatePage('Bug')

        when: "create bug"
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.createBug(name, description, area.name, [env.name, env1.name], "Web", action, result)

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        verifyAll {
            showPage.areaValue.text() == area.name
            showPage.projectValue.text() == project.name
            showPage.nameValue.text() == name
            showPage.descriptionValue.text() == description
            showPage.platformValue.text() == "Web"
            showPage.areEnvironmentsDisplayed([env.name, env1.name])
            showPage.stepsTable.isRowDisplayed(action, result)
        }
    }
}
