package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.StepTableModule

class EditBugPage extends BasePage {
    static url = "/bug/edit"
    static at = { title == "Edit Bug" }

    static content = {
        createButton { $("#create") }
        descriptionInput { $("#description")}
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=edit-home-link]") }
        listLink { $("[data-test-id=edit-list-link]") }
        nameInput { $("#name")}
        stepsTable { module StepTableModule }
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
    void editBug() {
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
