package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import com.github.javafaker.Faker
import geb.module.Select

class CreateTestCasePage extends BasePage {
    static url = "/testCase/create"
    static at = { title == "Create TestCase" }

    static content = {
        areaOptions { $("#area>option") }
        createButton { $("#create") }
        descriptionInput { $("#description") }
        errorsMessage { $("ul.errors") }
        executionMethodOptions { $("#executionMethod>option") }
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link]") }
        nameInput { $("#name") }
        projectOptions { $("#project>option") }
        testStepTable { module StepTableModule }
        typeOptions { $("#type>option") }
    }

    /**
     * select element strongly typed for convenience in tests
     */
    Select areaSelect() {
        $("#area").module(Select)
    }

    Select executionMethodSelect() {
        $("#executionMethod").module(Select)
    }

    Select projectSelect() {
        $("#project").module(Select)
    }

    Select typeSelect() {
        $("#type").module(Select)
    }

    /**
     * determines if the required field indication (asterisk) is
     * displayed for the supplied fields
     * @param fields - list of fields
     * @return - true if all fields have the indicator, false if at least one does not
     */
    boolean areRequiredFieldIndicatorsDisplayed(List<String> fields) {
        for(field in fields) {
            def sel = $("label[for=${field}]>span.required-indicator")
            if (!sel.displayed) {
                return false
            }
        }
        return true
    }

    /**
     * adds a generic test case
     */
    void createTestCase() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        projectSelect().selected = "1"
        testStepTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
        createButton.click()
    }

    /**
     * creates a test case with the supplied data
     */
    void createTestCase(String name, String description, String project, String area, String method, String type) {
        nameInput = name
        descriptionInput = description
        projectSelect().selected = project
        executionMethodSelect().selected = method
        typeSelect().selected = type
        areaSelect().selected = area
        createButton.click()
    }

    /**
     * fills in all information for the create form
     * but does not submit
     */
    void completeCreateForm() {
        nameInput = "fake test case"
        descriptionInput = "fake description"
        projectSelect().selected = "1"
        testStepTable.addStep("step action", "step result")
    }

    /**
     * Gets the labels for all fields displayed on the page
     * @return - a list of field names
     */
    List<String> getFields() {
        return fieldLabels*.text()
    }

    /**
     * clicks the home link
     */
    void goToHome() {
        homeLink.click()
    }

    /**
     * clicks the list link
     */
    void goToList() {
        listLink.click()
    }
}
