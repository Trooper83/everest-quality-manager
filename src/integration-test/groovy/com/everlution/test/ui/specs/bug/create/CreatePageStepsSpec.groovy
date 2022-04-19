package com.everlution.test.ui.specs.bug.create

import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.github.javafaker.Faker
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageStepsSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.navBar.goToCreatePage('Bug')
    }

    void "add test step row"() {
        expect: "row count is 0"
        def page = browser.page(CreateBugPage)
        page.stepsTable.getRowCount() == 0

        when: "add a test step row"
        page.stepsTable.addRow()

        then: "row count is 1"
        page.stepsTable.getRowCount() == 1
    }

    void "remove test step row"() {
        setup: "add a row"
        def page = browser.page(CreateBugPage)
        page.stepsTable.addRow()

        expect: "row count is 1"
        page.stepsTable.getRowCount() == 1

        when: "remove the first row"
        page.stepsTable.removeRow(0)

        then: "row count is 0"
        page.stepsTable.getRowCount() == 0
    }

    void "null action and result message"() {
        given: "get fake data"
        Faker faker = new Faker()
        def name = faker.zelda().game()
        def description = faker.zelda().character()

        when: "create bug"
        CreateBugPage createPage = at CreateBugPage
        createPage.createBug(name, description, "", [], "", "", "")

        then:
        createPage.errorsMessage.text() ==
                "Property [action] of class [class com.everlution.Step] with value [null] does not pass custom validation\nProperty [result] of class [class com.everlution.Step] with value [null] does not pass custom validation"
    }
}
