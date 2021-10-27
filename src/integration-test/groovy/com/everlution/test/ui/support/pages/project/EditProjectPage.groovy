package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class EditProjectPage extends BasePage {
    static url = "/project/edit"
    static at = { title == "Edit Project" }

    static content = {
        addAreaButton { $("#btnAddArea") }
        areaInput { $("#area") }
        areaRemovedInput { $("input[data-test-id='area-removed-input']") }
        areaTag(required: false) { text -> $("li", name: text) }
        areaTagEditButton { text -> areaTag(text).find("input[data-test-id='edit-area-button']") }
        areaTagInput { text -> areaTag(text).find("input[data-test-id='area-tag-input']") }
        areaTagRemoveButton { text -> areaTag(text).find("input[data-test-id='remove-area-button']") }
        areaTagSaveButton(required: false) { text -> areaTag(text).find("input[data-test-id='area-save-button']") }
        areaTags(required: false) { $("#areas li") }
        codeInput { $("#code") }
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=edit-home-link]") }
        listLink { $("[data-test-id=edit-list-link]") }
        nameInput { $("#name") }
        updateButton { $("[data-test-id=edit-update-button]")}
    }

    /**
     * adds an area tag
     * @param name - name of the tag to add
     */
    void addAreaTag(String name) {
        areaInput << name
        addAreaButton.click()
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
     * edits the value of an area tag
     * @param name - name of the tag to edit
     * @param value - the new value of the tag
     */
    void editAreaTag(String name, String value) {
        areaTagEditButton(name).click()
        areaTagInput(name).value(value)
        areaTagSaveButton(name).click()
    }

    /**
     * clicks the update button
     */
    void editProject() {
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

    /**
     * determines if an area tag is displayed
     * @param name - name of the tag
     * @return boolean - true if tag found, false if not
     */
    boolean isAreaTagDisplayed(String name) {
        return areaTag(name).size() == 1
    }

    /**
     * determines if an area tag has a hidden input
     * fails the test if the input is not found
     * present means found in the DOM, displayed means visible to the user
     * @param name - name of the tag
     * @return boolean - true if input is displayed, false if it is not displayed
     */
    boolean isAreaTagHiddenInputDisplayed(String name) {
        def e = areaTagInput(name)
        assert e.size() == 1 //verify one tag is found
        return e.displayed
    }

    /**
     * removes an area tag
     * @param name - name of the tag to remove
     */
    void removeAreaTag(String name) {
        areaTagRemoveButton(name).click()
    }
}
