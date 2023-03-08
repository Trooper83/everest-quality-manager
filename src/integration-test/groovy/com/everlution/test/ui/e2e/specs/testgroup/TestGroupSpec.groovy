package com.everlution.test.ui.e2e.specs.testgroup

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.EditTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec

class TestGroupSpec extends GebSpec {

    def group = DataFactory.testGroup()

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 'Atlas')

        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Test Group")

        def createPage = browser.page(CreateTestGroupPage)
        createPage.createTestGroup(group.name)
    }

    void "test group is created and data persists"() {
        expect: "data is displayed on show page"
        def showPage = at ShowTestGroupPage
        showPage.nameValue.text() == group.name
    }

    void "test group can be edited and data persists"() {
        given:
        def showPage = at ShowTestGroupPage
        showPage.goToEdit()

        when:
        def editPage = browser.page(EditTestGroupPage)
        def testGroup = DataFactory.testGroup()
        editPage.editTestGroup(testGroup.name)

        then: "data is displayed on show page"
        showPage.nameValue.text() == testGroup.name
    }

    void "test group can be deleted"() {
        when:
        def showPage = at ShowTestGroupPage
        showPage.delete()

        then:
        def list = at ListTestGroupPage
        !list.listTable.isValueInColumn("Name", group.name)
    }
}
