package com.everlution.test.ui.support.pages.common

import com.everlution.test.ui.support.pages.modules.ProjectNavModule

class EditPage extends BasePage {
    static content = {
        fieldLabels { $("fieldset label") }
        projectNavButtons { module ProjectNavModule }
        updateButton { $("[data-test-id=edit-update-button]") }
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
    void edit() {
        updateButton.click()
    }

    /**
     * Gets the labels for all fields displayed on the page
     * @return - a list of field names
     */
    List<String> getFields() {
        return fieldLabels*.text()
    }
}
