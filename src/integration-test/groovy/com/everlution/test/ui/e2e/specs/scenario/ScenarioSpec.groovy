package com.everlution.test.ui.e2e.specs.scenario

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import com.everlution.test.ui.support.pages.scenario.EditScenarioPage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec

class ScenarioSpec extends GebSpec {

    String area = 'Scenarios'
    String env = 'Integrated'
    String platform = 'Web'
    def scn = DataFactory.scenario()

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 'Atlas')

        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain("Scenarios")

        def scenarios = at ListScenarioPage
        scenarios.createButton.click()

        CreateScenarioPage createPage = browser.page(CreateScenarioPage)
        createPage.createScenario(scn.name, scn.description, scn.gherkin, area, [env], scn.executionMethod,
                scn.type, platform)
    }

    void "scenario is created and data persists"() {
        expect: "data is displayed on show page"
        def showPage = at ShowScenarioPage
        verifyAll {
            showPage.areaValue.text() == area
            showPage.nameValue.text() == scn.name
            showPage.descriptionValue.text() == scn.description
            showPage.executionMethodValue.text() == scn.executionMethod
            showPage.typeValue.text() == scn.type
            showPage.platformValue.text() == platform
            showPage.areEnvironmentsDisplayed([env])
            showPage.gherkinTextArea.text() == scn.gherkin
        }
    }

    void "scenario can be edited and data persists"() {
        given:
        def showPage = at ShowScenarioPage
        showPage.goToEdit()

        when:
        def editPage = browser.page(EditScenarioPage)
        def scenario = DataFactory.scenario()
        editPage.editScenario(scenario.name, scenario.description, scenario.gherkin, '', [''],
                'Manual', 'API', 'iOS')

        then: "data is displayed on show page"
        verifyAll {
            showPage.areaValue.text() == ''
            showPage.nameValue.text() == scenario.name
            showPage.descriptionValue.text() == scenario.description
            showPage.executionMethodValue.text() == 'Manual'
            showPage.typeValue.text() == 'API'
            showPage.platformValue.text() == 'iOS'
            !showPage.areEnvironmentsDisplayed([env])
            showPage.gherkinTextArea.text() == scenario.gherkin
        }
    }

    void "scenario can be deleted"() {
        when:
        def showPage = at ShowScenarioPage
        showPage.delete()

        then:
        ListScenarioPage list = at ListScenarioPage
        !list.listTable.isValueInColumn("Name", scn.name)
    }
}
