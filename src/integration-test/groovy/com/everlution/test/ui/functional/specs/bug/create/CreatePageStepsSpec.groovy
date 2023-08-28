package com.everlution.test.ui.functional.specs.bug.create

import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.data.Credentials
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
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")
    }

    void "add test step row"() {
        expect: "row count is 0"
        def page = browser.page(CreateBugPage)
        page.stepsTable.getStepsCount() == 0

        when: "add a test step row"
        page.scrollToBottom()
        page.stepsTable.addRow()

        then: "row count is 1"
        page.stepsTable.getStepsCount() == 1
    }

    void "remove test step row"() {
        setup: "add a row"
        def page = browser.page(CreateBugPage)
        page.scrollToBottom()
        page.stepsTable.addRow()

        expect: "row count is 1"
        page.stepsTable.getStepsCount() == 1

        when: "remove the first row"
        page.stepsTable.removeRow(0)

        then: "row count is 0"
        page.stepsTable.getStepsCount() == 0
    }

    void "null action and result message"() {
        given: "get fake data"
        Faker faker = new Faker()
        def name = faker.zelda().game()
        def description = faker.zelda().character()

        when: "create bug"
        CreateBugPage createPage = at CreateBugPage
        createPage.createBug(name, description, "", [], "", "", "", "actual", "expected")

        then:
        createPage.errorsMessage*.text() ==
                ["Property [act] with value [null] does not pass custom validation",
                "Property [result] with value [null] does not pass custom validation"]
    }

    void "remove button only displayed for last step"() {
        setup: "add a row"
        def page = browser.page(CreateBugPage)
        page.scrollToBottom()
        page.stepsTable.addRow()

        expect:
        page.stepsTable.getStep(0).find('input[value=Remove]').displayed

        when:
        page.stepsTable.addRow()

        then:
        !page.stepsTable.getStep(0).find('input[value=Remove]').displayed
        page.stepsTable.getStep(1).find('input[value=Remove]').displayed
    }

    void "alt+n adds new step row with action input focused"() {
        expect:
        def page = browser.page(CreateBugPage)
        page.stepsTable.getStepsCount() == 0

        when: "add a row"
        page.stepsTable.addRowHotKey()

        then:
        page.stepsTable.getStepsCount() == 1
        page.stepsTable.getStep(0).find('textarea[name="steps[0].act"]').focused
    }
}