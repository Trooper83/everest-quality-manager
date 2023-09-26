package com.everlution.test.ui.e2e.specs.testcase

import com.everlution.Step
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.step.CreateStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec

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
                tc.type, platform, [new Step(act: 'Login to app', result: 'You are logged into the app')])

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
            showPage.isStepsRowDisplayed('Login to app', 'You are logged into the app')
        }
    }

    void "test case can be edited and data persists"() {
        given:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.createFreeFormTestCase(tc.name, tc.description, area, [env], ['Regression'], tc.executionMethod,
                tc.type, platform, [new Step(act: 'Login to app', result: 'You are logged into the app')])
        def showPage = at ShowTestCasePage
        showPage.goToEdit()

        when:
        def editPage = browser.page(EditTestCasePage)
        def testCase = DataFactory.scenario()
        editPage.editTestCase(testCase.name, testCase.description, '', [''],
                'Manual', 'API', 'iOS', [''])

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
        }
    }

    void "test case can be deleted"() {
        given:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.createFreeFormTestCase(tc.name, tc.description, area, [env], ['Regression'], tc.executionMethod,
                tc.type, platform, [new Step(act: 'Login to app', result: 'You are logged into the app')])

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
        createPage.sideBar.goToCreate('Step')
        browser.page(CreateStepPage).createStep(step.action, step.name, step.result)

        when:
        browser.page(ShowStepPage).sideBar.goToCreate('Test Case')
        createPage.createBuilderTestCase(tc.name, tc.description, area, [env], ['Regression'], tc.executionMethod,
                tc.type, platform, [step.name])

        then: "data is displayed on show page"
        def showPage = at ShowTestCasePage
        showPage.isStepsRowDisplayed(step.action, step.result)
    }
}
