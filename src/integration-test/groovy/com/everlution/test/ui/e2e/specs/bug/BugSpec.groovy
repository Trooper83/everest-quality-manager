package com.everlution.test.ui.e2e.specs.bug

import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.github.javafaker.Faker
import geb.spock.GebSpec

@SendResults
class BugSpec extends GebSpec {

    Faker faker = new Faker()
    def description = faker.zelda().character()
    def action = faker.lorem().sentence(5)
    def data = faker.lorem().sentence(5)
    def result = faker.lorem().sentence(7)
    def actual = faker.lorem().sentence(4)
    def expected = faker.lorem().sentence(4)
    def name = faker.lorem().sentence(5)
    def notes = faker.lorem().sentence(5)
    def area = 'Bugs'
    def env = 'Integrated'
    def env1 = 'Production'

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 'Atlas')

        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")
    }

    void "bug can be created and data persists"() {
        when:
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.createFreeFormBug(name, description, area, [env, env1], "Web", notes, action, data, result, actual, expected)

        then: "data is displayed on show page"
        ShowBugPage showPage = at ShowBugPage
        verifyAll {
            showPage.areaValue.text() == area
            showPage.nameValue.text() == name
            showPage.descriptionValue.text() == description
            showPage.platformValue.text() == "Web"
            showPage.statusValue.text() == "Open"
            showPage.areEnvironmentsDisplayed([env, env1])
            showPage.notesValue.text() == notes
            showPage.isStepsRowDisplayed(action, data, result)
            showPage.expectedValue.text() == expected
            showPage.actualValue.text() == actual
        }
    }

    void "bug can be edited and data persists"() {
        given:
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.createFreeFormBug(name, description, area, [env, env1], "Web", notes, action, data, result, actual, expected)
        ShowBugPage showPage = at ShowBugPage
        showPage.goToEdit()

        when: "edit all bug data"
        EditBugPage editPage = browser.page(EditBugPage)
        def data = DataFactory.bug()
        editPage.editBug(data.name, data.description, "",[""], "", "", data.expected, data.actual)

        then: "data is displayed on show page"
        verifyAll {
            showPage.areaValue.text() == ""
            showPage.nameValue.text() == data.name
            showPage.platformValue.text() == ""
            showPage.statusValue.text() == "Closed"
            showPage.descriptionValue.text() == data.description
            !showPage.areEnvironmentsDisplayed([env, env1])
            showPage.expectedValue.text() == data.expected
            showPage.actualValue.text() == data.actual
        }
    }

    void "bug can be deleted"() {
        given:
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.createFreeFormBug(name, description, area, [env, env1], "Web", notes, action, data, result, actual, expected)

        when: "delete bug"
        def showPage = browser.at(ShowBugPage)
        showPage.nameValue.text() == name
        showPage.delete()

        then: "at list page"
        def listPage = at ListBugPage
        !listPage.listTable.isValueInColumn("Name", name)
    }

    void "adding builder steps retains order"() {
        given:
        def createPage = browser.page(CreateBugPage)
        createPage.createBuilderBug("This should not fail bug", "", "", [], "",
                "This is a builder step", "", "")

        when:
        def show = at ShowBugPage
        show.goToEdit()
        def editPage = browser.page(EditBugPage)
        editPage.scrollToBottom()
        editPage.stepsTable.addBuilderStep("This is a builder step")
        editPage.edit()

        then:
        at ShowBugPage
    }

    void "adding builder step from suggested steps retains order"() {
        given:
        def createPage = browser.page(CreateBugPage)
        createPage.createBuilderBug("This should not fail bug", "", "", [], "",
                "This is a builder step", "", "")

        when:
        def show = at ShowBugPage
        show.goToEdit()
        def editPage = browser.page(EditBugPage)
        editPage.scrollToBottom()
        editPage.stepsTable.selectSuggestedStep()
        editPage.edit()

        then:
        at ShowBugPage
    }
}
