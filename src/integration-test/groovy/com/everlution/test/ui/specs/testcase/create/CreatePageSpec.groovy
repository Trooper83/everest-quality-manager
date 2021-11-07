package com.everlution.test.ui.specs.testcase.create

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.common.HomePage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageSpec extends GebSpec {

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to the create page"
        to CreateTestCasePage
    }

    void "home link directs to home view"() {
        when: "click the home button"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.goToHome()

        then: "at the home page"
        at HomePage
    }

    void "list link directs to list view"() {
        when: "click the list link"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.goToList()

        then: "at the list page"
        at ListTestCasePage
    }

    void "required fields indicator displayed for required fields"() {
        expect: "required field indicators displayed"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.areRequiredFieldIndicatorsDisplayed(["executionMethod", "name", "type", "project"])
    }

    void "verify method and type field options"() {
        expect: "correct options populate for executionMethod and type"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        verifyAll {
            page.executionMethodOptions*.text() == ["Automated", "Manual"]
            page.typeOptions*.text() == ["UI", "API"]
        }

        and: "default values are correct"
        verifyAll( {
            page.executionMethodSelect().selected == "Automated"
            page.typeSelect().selected == "UI"
        })
    }

    void "add test step row"() {
        expect: "row count is 0"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.getRowCount() == 0

        when: "add a test step row"
        page.testStepTable.addRow()

        then: "row count is 1"
        page.testStepTable.getRowCount() == 1
    }

    void "remove test step row"() {
        setup: "add a row"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.testStepTable.addRow()

        expect: "row count is 1"
        page.testStepTable.getRowCount() == 1

        when: "remove the first row"
        page.testStepTable.removeRow(0)

        then: "row count is 1"
        page.testStepTable.getRowCount() == 0
    }

    void "correct fields are displayed"() {
        expect: "correct fields are displayed"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        page.getFields() == ["Project *", "Area", "Description", "Execution Method *", "Name *", "Type *"]
    }

    void "project field has no value set"() {
        expect: "default text selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selectedText == "Select a Project..."
        page.projectSelect().selected == ""
    }

    void "failed form submission populates projects"() {
        when: "form submission without project selected"
        def page = browser.page(CreateTestCasePage)
        page.nameInput = "failure"
        page.createButton.click()

        then: "projects are populated"
        page.projectOptions.size() > 1
    }

    void "null project message displays"() {
        when: "form submission without project selected"
        def page = browser.page(CreateTestCasePage)
        page.nameInput = "failure"
        page.createButton.click()

        then: "projects are populated"
        page.errorsMessage.text() == "Property [project] of class [class com.everlution.TestCase] cannot be null"
    }

    void "area field has no value set"() {
        when: "project is selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = "1"

        then: "default text selected"
        page.areaSelect().selectedText == "Select an Area..."
        page.areaSelect().selected == ""
    }

    void "area field defaults disabled"() {
        expect: "area is disabled"
        def page = browser.page(CreateTestCasePage)
        page.areaSelect().disabled
    }

    void "area field disabled and depopulated when project is set to default"() {
        given: "project is selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = "bootstrap project"

        expect: "area field enabled and populated"
        waitFor(2) { //need to wait for transition
            !page.areaSelect().disabled
            page.areaOptions.size() > 1
        }

        when: "project set to default"
        page.projectSelect().selected = ""

        then: "area is disabled, depopulated and set to default"
        waitFor(2) { //need to wait for transition
            page.areaSelect().disabled
            page.areaOptions.size() == 1
            page.areaSelect().selectedText == "Select an Area..."
            page.areaSelect().selected == ""
        }
    }
}
