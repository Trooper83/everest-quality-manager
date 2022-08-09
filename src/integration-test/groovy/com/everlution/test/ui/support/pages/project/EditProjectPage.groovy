package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class EditProjectPage extends BasePage {
    static url = "/project/edit"
    static at = { title == "Edit Project" }

    static content = {
        addAreaButton { $("#btnAddArea") }
        areaInput { $("#area") }
        areaRemovedInput { $("input[data-test-id='removed-tag-input']") }
        areaTag(required: false) { text -> $("#areas li", name: text) }
        areaTagCancelButton { text -> areaTag(text).find("[data-test-id='cancel-tag-button']") }
        areaTagEditButton(required: false) { text -> areaTag(text).find("[data-test-id='edit-tag-button']") }
        areaTagInput { text -> areaTag(text).find("[data-test-id='tag-input']") }
        areaTagOptionsButton { text -> areaTag(text).find("svg") }
        areaTagRemoveButton(required: false) { text -> areaTag(text).find("[data-test-id='remove-tag-button']") }
        areaTagSaveButton(required: false) { text -> areaTag(text).find("[data-test-id='save-tag-button']") }
        areaTags(required: false) { $("#areas li") }
        codeInput { $("#code") }
        addEnvironmentButton { $("#btnAddEnv") }
        environmentInput { $("#environment") }
        environmentRemovedInput { $("input[data-test-id='removed-tag-input']") }
        environmentTag(required: false) { text -> $("#environments li", name: text) }
        environmentTagCancelButton { text -> environmentTag(text).find("[data-test-id='cancel-tag-button']") }
        environmentTagEditButton(required: false) { text -> environmentTag(text).find("[data-test-id='edit-tag-button']") }
        environmentTagInput { text -> environmentTag(text).find("[data-test-id='tag-input']") }
        environmentTagOptionsButton { text -> environmentTag(text).find("svg") }
        environmentTagRemoveButton(required: false) { text -> environmentTag(text).find("[data-test-id='remove-tag-button']") }
        environmentTagSaveButton(required: false) { text -> environmentTag(text).find("[data-test-id='save-tag-button']") }
        environmentTags(required: false) { $("#environments li") }
        errorMessages { $("ul.errors") }
        fieldLabels { $("fieldset label") }
        nameInput { $("#name") }
        tooltip(wait: true) { $("div.tooltip-inner") }
        updateButton { $("[data-test-id=edit-update-button]") }
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
     * adds an environment tag
     * @param name - name of the tag to add
     */
    void addEnvironmentTag(String name) {
        environmentInput << name
        addEnvironmentButton.click()
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
     * cancels area tag editing
     * @param name
     */
    void cancelAreaTagEdit(String name) {
        areaTagCancelButton(name).click()
    }

    /**
     * cancels env tag editing
     * @param name
     */
    void cancelEnvironmentTagEdit(String name) {
        environmentTagCancelButton(name).click()
    }

    /**
     * displays the edit options
     */
    void displayAreaTagEditOptions(String name) {
        areaTagOptionsButton(name).click()
    }

    /**
     * displays the area tag edit fields
     * @param name
     */
    void displayAreaTagEditFields(String name) {
        areaTagOptionsButton(name).click()
        areaTagEditButton(name).click()
    }

    /**
     * displays the env tag edit fields
     * @param name
     */
    void displayEnvironmentTagEditFields(String name) {
        environmentTagOptionsButton(name).click()
        environmentTagEditButton(name).click()
    }

    /**
     * displays the env edit options
     * @param name
     */
    void displayEnvironmentTagEditOptions(String name) {
        environmentTagOptionsButton(name).click()
    }

    /**
     * edits the value of an area tag
     * @param name - name of the tag to edit
     * @param value - the new value of the tag
     */
    void editAreaTag(String name, String value) {
        areaTagOptionsButton(name).click()
        areaTagEditButton(name).click()
        areaTagInput(name).value(value)
        areaTagSaveButton(name).click()
    }

    /**
     * edits the value of an environment tag
     * @param name - name of the tag to edit
     * @param value - the new value of the tag
     */
    void editEnvironmentTag(String name, String value) {
        environmentTagOptionsButton(name).click()
        environmentTagEditButton(name).click()
        environmentTagInput(name).value(value)
        environmentTagSaveButton(name).click()
    }

    /**
     * clicks the update button
     */
    void editProject() {
        updateButton.click()
    }

    /**
     * edits a projects with the supplied data
     */
    void editProject(String name, String code) {
        nameInput = name
        codeInput = code
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
     * gets the text of the displayed tooltip
     * @return
     */
    String getToolTipText() {
        waitFor() {
            tooltip.displayed
        }
        return tooltip.text()
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
     * determines if an environment tag is displayed
     * @param name - name of the tag
     * @return boolean - true if tag found, false if not
     */
    boolean isEnvironmentTagDisplayed(String name) {
        return environmentTag(name).size() == 1
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
     * determines if an environment tag has a hidden input
     * fails the test if the input is not found
     * present means found in the DOM, displayed means visible to the user
     * @param name - name of the tag
     * @return boolean - true if input is displayed, false if it is not displayed
     */
    boolean isEnvironmentTagHiddenInputDisplayed(String name) {
        def e = environmentTagInput(name)
        assert e.size() == 1 //verify one tag is found
        return e.displayed
    }

    /**
     * removes an area tag
     * @param name - name of the tag to remove
     */
    void removeAreaTag(String name) {
        areaTagOptionsButton(name).click()
        areaTagRemoveButton(name).click()
    }

    /**
     * removes an environment tag
     * @param name - name of the tag to remove
     */
    void removeEnvironmentTag(String name) {
        environmentTagOptionsButton(name).click()
        environmentTagRemoveButton(name).click()
    }
}
