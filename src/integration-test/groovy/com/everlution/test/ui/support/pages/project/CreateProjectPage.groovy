package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class CreateProjectPage extends BasePage {
    static url = "/project/create"
    static at = { title == "Create Project" }

    static content = {
        addAreaButton { $("#btnAddArea") }
        areaInput { $("#area") }
        areaTag(required: false) { text -> $("#areas li", name: text) }
        areaTagEditButton { text -> areaTag(text).find("input[data-test-id='edit-tag-button']") }
        areaTagInput { text -> areaTag(text).find("input[data-test-id='tag-input']") }
        areaTagRemoveButton { text -> areaTag(text).find("input[data-test-id='remove-tag-button']") }
        areaTagSaveButton(required: false) { text -> areaTag(text).find("input[data-test-id='save-tag-button']") }
        areaTags(required: false) { $("#areas li") }
        codeInput { $("#code") }
        createButton { $("#create") }
        addEnvironmentButton { $("#btnAddEnv") }
        environmentInput { $("#environment") }
        environmentTag(required: false) { text -> $("#environments li", name: text) }
        environmentTagEditButton { text -> environmentTag(text).find("input[data-test-id='edit-tag-button']") }
        environmentTagInput { text -> environmentTag(text).find("input[data-test-id='tag-input']") }
        environmentRemoveButton { text -> environmentTag(text).find("input[data-test-id='remove-tag-button']") }
        environmentTagSaveButton(required: false) { text -> environmentTag(text).find("input[data-test-id='save-tag-button']") }
        environmentTags(required: false) { $("#environments li") }
        errors { $("ul.errors>li") }
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link]") }
        nameInput { $("#name") }
        tooltip { $("div.tooltip-inner") }
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
     * edits the value of an environment tag
     * @param name - name of the tag to edit
     * @param value - the new value of the tag
     */
    void editEnvironmentTag(String name, String value) {
        environmentTagEditButton(name).click()
        environmentTagInput(name).value(value)
        environmentTagSaveButton(name).click()
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
        areaTagRemoveButton(name).click()
    }

    /**
     * removes an environment tag
     * @param name - name of the tag to remove
     */
    void removeEnvironmentTag(String name) {
        environmentRemoveButton(name).click()
    }

    /**
     * clicks the create button
     */
    void submitForm() {
        createButton.click()
    }
}
