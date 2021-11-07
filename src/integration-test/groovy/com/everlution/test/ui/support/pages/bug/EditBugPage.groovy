package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import geb.module.Select

class EditBugPage extends BasePage {
    static url = "/bug/edit"
    static at = { title == "Edit Bug" }

    static content = {
        areaOptions { $("#area>option") }
        descriptionInput { $("#description")}
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=edit-home-link]") }
        listLink { $("[data-test-id=edit-list-link]") }
        nameInput { $("#name")}
        projectNameField { $("[data-test-id=edit-project-name]") }
        stepRemovedInput { $("input[data-test-id='step-removed-input']") }
        stepsTable { module StepTableModule }
        updateButton { $("[data-test-id=edit-update-button]") }
    }

    /**
     * select element strongly typed for convenience in tests
     */
    Select areaSelect() {
        $("#area").module(Select)
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
    void editBug() {
        updateButton.click()
    }

    /**
     * edits a bug with the supplied data
     */
    void editBug(String name, String description, String area) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
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
