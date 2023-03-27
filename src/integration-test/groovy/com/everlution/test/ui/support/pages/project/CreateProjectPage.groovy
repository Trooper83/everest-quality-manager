package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class CreateProjectPage extends BasePage {
    static url = "/project/create"
    static at = { title == "Create Project" }

    static content = {
        addAreaButton { $("#btnAddArea") }
        areaInput { $("#area") }
        areaTag(required: false) { text -> $("#areas li", name: text) }
        areaTagCancelButton { text -> areaTag(text).find("[data-test-id='cancel-tag-button']") }
        areaTagEditButton(required: false) { text -> areaTag(text).find("[data-test-id='edit-tag-button']") }
        areaTagInput { text -> areaTag(text).find("[data-test-id='tag-input']") }
        areaTagOptionsButton { text -> areaTag(text).find("svg") }
        areaTagRemoveButton(required: false) { text -> areaTag(text).find("[data-test-id='remove-tag-button']") }
        areaTagSaveButton(required: false) { text -> areaTag(text).find("[data-test-id='save-tag-button']") }
        areaTags(required: false) { $("#areas li") }
        codeInput { $("#code") }
        createButton { $("#create") }
        addEnvironmentButton { $("#btnAddEnv") }
        environmentInput { $("#environment") }
        environmentTag(required: false) { text -> $("#environments li", name: text) }
        environmentTagCancelButton { text -> environmentTag(text).find("[data-test-id='cancel-tag-button']") }
        environmentTagEditButton(required: false) { text -> environmentTag(text).find("[data-test-id='edit-tag-button']") }
        environmentTagInput { text -> environmentTag(text).find("[data-test-id='tag-input']") }
        environmentTagOptionsButton { text -> environmentTag(text).find("svg") }
        environmentTagRemoveButton(required: false) { text -> environmentTag(text).find("[data-test-id='remove-tag-button']") }
        environmentTagSaveButton(required: false) { text -> environmentTag(text).find("[data-test-id='save-tag-button']") }
        environmentTags(required: false) { $("#environments li") }
        errors { $("ul.alert-danger>li") }
        nameInput { $("#name") }
        tooltip(wait: true) { $("div.tooltip-inner") }
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
     * fills in all fields for the create form but does not submit
     */
    void completeCreateForm(String name, String code) {
        nameInput << name
        codeInput << code
    }

    /**
     * fills in all fields for the create form and submits
     */
    void createProject(String name, String code) {
        this.completeCreateForm(name, code)
        createButton.click()
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
     * displays the edit options
     */
    void displayAreaTagEditOptions(String name) {
        areaTagOptionsButton(name).click()
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
     * determines if an area tag is displayed
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

    /**
     * clicks the create button
     */
    void submitForm() {
        createButton.click()
    }
}
