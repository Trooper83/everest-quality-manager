package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import geb.module.Select

class EditTestCasePage extends BasePage {
    static url = "/testCase/edit"
    static at = { title == "Edit TestCase" }

    static content = {
        areaOptions { $("#area>option") }
        descriptionInput { $("#description") }
        executionMethodOptions { $("#executionMethod>option") }
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=edit-home-link]") }
        listLink { $("[data-test-id=edit-list-link]") }
        nameInput { $("#name") }
        projectNameField { $("[data-test-id=edit-project-name]") }
        stepRemovedInput { $("input[data-test-id='step-removed-input']") }
        testStepTable { module StepTableModule }
        typeOptions { $("#type>option") }
        updateButton { $("[data-test-id=edit-update-button]")}
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
     * clicks the update button
     */
    void editTestCase() {
        updateButton.click()
    }

    /**
     * edits a test case with the supplied data
     */
    void editTestCase(String name, String description, String area, String method, String type) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        executionMethodSelect().selected = method
        typeSelect().selected = type
        updateButton.click()
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
