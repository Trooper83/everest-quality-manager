package com.manager.quality.everest.test.ui.e2e.specs.testcase

import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.ui.support.pages.stepTemplate.CreateStepTemplatePage
import com.manager.quality.everest.test.ui.support.pages.stepTemplate.ShowStepTemplatePage
import com.manager.quality.everest.test.ui.support.pages.testcase.CreateTestCasePage
import com.manager.quality.everest.test.ui.support.pages.testcase.EditTestCasePage
import com.manager.quality.everest.test.ui.support.pages.testcase.ListTestCasePage
import com.manager.quality.everest.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec

@SendResults
class TestCaseSpec extends GebSpec {

    String area = 'Tests'
    String env = 'Integrated'
    String platform = 'Web'
    def tc = DataFactory.testCase()

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 'Atlas')

        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Test Case")
    }

    void "test case is created and data persists"() {
        when:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.createFreeFormTestCase(tc.name, tc.description, area, [env], ['Regression'], tc.executionMethod,
                tc.type, platform, [new Step(act: 'Login to app', data: 'test', result: 'You are logged into the app')], tc.verify)

        then: "data is displayed on show page"
        def showPage = at ShowTestCasePage
        verifyAll {
            showPage.areaValue.text() == area
            showPage.nameValue.text() == tc.name
            showPage.descriptionValue.text() == tc.description
            showPage.executionMethodValue.text() == tc.executionMethod
            showPage.typeValue.text() == tc.type
            showPage.platformValue.text() == platform
            showPage.areEnvironmentsDisplayed([env])
            showPage.areTestGroupsDisplayed(['Regression'])
            showPage.isStepsRowDisplayed('Login to app', 'test', 'You are logged into the app')
            showPage.verifyValue.text() == tc.verify
        }
    }

    void "test case can be edited and data persists"() {
        given:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.createFreeFormTestCase(tc.name, tc.description, area, [env], ['Regression'], tc.executionMethod,
                tc.type, platform, [new Step(act: 'Login to app', data: 'test', result: 'You are logged into the app')], tc.verify)
        def showPage = at ShowTestCasePage
        showPage.goToEdit()

        when:
        def editPage = browser.page(EditTestCasePage)
        def testCase = DataFactory.scenario()
        editPage.editTestCase(testCase.name, testCase.description, '', [''],
                'Manual', 'API', 'iOS', [''], "testing")

        then: "data is displayed on show page"
        verifyAll {
            showPage.areaValue.text() == ''
            showPage.nameValue.text() == testCase.name
            showPage.descriptionValue.text() == testCase.description
            showPage.executionMethodValue.text() == 'Manual'
            showPage.typeValue.text() == 'API'
            showPage.platformValue.text() == 'iOS'
            !showPage.areEnvironmentsDisplayed([env])
            !showPage.areTestGroupsDisplayed(['Regression'])
            showPage.verifyValue.text() == 'testing'
        }
    }

    void "test case can be deleted"() {
        given:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.createFreeFormTestCase(tc.name, tc.description, area, [env], ['Regression'], tc.executionMethod,
                tc.type, platform, [new Step(act: 'Login to app', data: 'test', result: 'You are logged into the app')], tc.verify)

        when:
        def showPage = at ShowTestCasePage
        showPage.delete()

        then:
        ListTestCasePage list = at ListTestCasePage
        !list.listTable.isValueInColumn("Name", tc.name)
    }

    void "test case with builder steps persists steps"() {
        given:
        def step = DataFactory.step()
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.sideBar.goToCreate('Step Template')
        browser.page(CreateStepTemplatePage).createStepTemplate(step.action, step.name, step.result)

        when:
        browser.page(ShowStepTemplatePage).sideBar.goToCreate('Test Case')
        createPage.createBuilderTestCase(tc.name, tc.description, area, [env], ['Regression'], tc.executionMethod,
                tc.type, platform, [step.name])

        then: "data is displayed on show page"
        def showPage = at ShowTestCasePage
        showPage.isStepsRowDisplayed(step.action, '', step.result)
    }

    void "adding builder steps retains order"() {
        given:
        def step = DataFactory.step()
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.sideBar.goToCreate('Step Template')
        browser.page(CreateStepTemplatePage).createStepTemplate(step.action, step.name, step.result)
        browser.page(ShowStepTemplatePage).sideBar.goToCreate('Test Case')
        createPage.createBuilderTestCase(tc.name, "", "", [], [], "",
                "", "", [step.name])

        when:
        def showPage = at ShowTestCasePage
        showPage.goToEdit()
        def editPage = browser.page(EditTestCasePage)
        editPage.scrollToBottom()
        editPage.stepsTable.addBuilderStep(step.name)
        editPage.stepsTable.appendBuilderSteps()
        editPage.edit()

        then:
        at ShowTestCasePage
    }
}
