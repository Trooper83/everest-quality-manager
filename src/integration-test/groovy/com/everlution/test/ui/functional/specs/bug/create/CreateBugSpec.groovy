package com.everlution.test.ui.functional.specs.bug.create

import com.everlution.domains.Area
import com.everlution.domains.Environment
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.support.data.Credentials
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
        projectHomePage.sideBar.goToCreate("Bug")

        when: "create a bug"
        def page = at CreateBugPage
        page.createFreeFormBug()

        then: "at show page"
        at ShowBugPage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
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
        def stepData = DataFactory.step()
        def actual = faker.lorem().sentence(7)
        def expected = faker.lorem().sentence(7)
        def notes = faker.lorem().sentence(7)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', project.name)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")

        when: "create bug"
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.createFreeFormBug(name, description, area.name, [env.name, env1.name], "Web", notes, stepData.action,
                stepData.data, stepData.result, actual, expected)

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        verifyAll {
            showPage.areaValue.text() == area.name
            showPage.nameValue.text() == name
            showPage.descriptionValue.text() == description
            showPage.platformValue.text() == "Web"
            showPage.notesValue.text() == notes
            showPage.statusValue.text() == "Open"
            showPage.areEnvironmentsDisplayed([env.name, env1.name])
            showPage.isStepsRowDisplayed(stepData.action, stepData.data, stepData.result)
            showPage.actualValue.text() == actual
            showPage.expectedValue.text() == expected
        }
    }
}
