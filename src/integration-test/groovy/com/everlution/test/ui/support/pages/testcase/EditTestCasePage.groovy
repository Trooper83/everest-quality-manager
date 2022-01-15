package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import geb.module.MultipleSelect
import geb.module.Select

class EditTestCasePage extends BasePage {
    static url = "/testCase/edit"
    static at = { title == "Edit TestCase" }

    static content = {
        areaOptions { $("#area>option") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        executionMethodOptions { $("#executionMethod>option") }
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=edit-home-link]") }
        listLink { $("[data-test-id=edit-list-link]") }
        nameInput { $("#name") }
        platformOptions { $("#platform>option") }
        projectNameField { $("[data-test-id=edit-project-name]") }
        stepRemovedInput { $("input[data-test-id='step-removed-input']") }
        stepsTable { module StepTableModule }
        testGroupsOptions { $("#testGroups>option") }
        typeOptions { $("#type>option") }
        updateButton { $("[data-test-id=edit-update-button]")}
    }

    /**
     * select element strongly typed for convenience in tests
     */
    Select areaSelect() {
        $("#area").module(Select)
    }

    MultipleSelect environmentsSelect() {
        $("#environments").module(MultipleSelect)
    }

    Select executionMethodSelect() {
        $("#executionMethod").module(Select)
    }

    Select platformSelect() {
        $("#platform").module(Select)
    }

    MultipleSelect testGroupsSelect() {
        $("#testGroups").module(MultipleSelect)
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
     * fills in all data but does not submit the form
     */
    void completeEditForm(String name, String description, String area, List<String> environments, String method,
                          String type, String platform, List<String> testGroups) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        environmentsSelect().selected = environments
        testGroupsSelect().selected = testGroups
        executionMethodSelect().selected = method
        platformSelect().selected = platform
        typeSelect().selected = type
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
    void editTestCase(String name, String description, String area, List<String> environments, String method,
                      String type, String platform, List<String> testGroups) {
        completeEditForm(name, description, area, environments, method, type, platform, testGroups)
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
