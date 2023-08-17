package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class EditProjectPage extends BasePage {
    static url = "/project/edit"
    static at = { title == "Edit Project" }

    static content = {
        addAreaButton { $("#btnAddArea") }
        areaInput { $("#area") }
        areaItem { name -> $("#areaRow li", name: name) }
        areaRemovedInput { $("input[data-test-id='removed-tag-input']") }
        areaTag(required: false) { text -> $("#areaRow p", text: text) }
        areaTagInput { text -> areaItem(text).find("[data-test-id='tag-input']") }
        areaTagRemoveButton { text -> areaTag(text).find("svg") }
        codeInput { $("#code") }
        addEnvironmentButton { $("#btnAddEnv") }
        environmentInput { $("#environment") }
        environmentItem { name -> $("#environmentRow li", name: name) }
        environmentRemovedInput { $("input[data-test-id='removed-tag-input']") }
        environmentTag(required: false) { text -> $("#environmentRow p", text: text) }
        environmentTagInput { text -> environmentItem(text).find("[data-test-id='tag-input']") }
        environmentTagRemoveButton { text -> environmentTag(text).find("svg") }
        errorMessages { $("div.alert-danger") }
        nameInput { $("#name") }
        tooltip(wait: true) { $("div[role=tooltip]") }
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
     * clicks the update button
     */
    void editProject() {
        updateButton.click()
    }

    /**
     * edits a projects with the supplied data
     */
    void editProject(String name, String code, List<String> areas, List<String> environments) {
        nameInput = name
        codeInput = code
        areas.each { addAreaTag(it) }
        environments.each { addEnvironmentTag(it) }
        updateButton.click()
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
        def tag = areaTag(name)
        return tag.size() == 1 && tag.displayed
    }

    /**
     * determines if an environment tag is displayed
     * @param name - name of the tag
     * @return boolean - true if tag found, false if not
     */
    boolean isEnvironmentTagDisplayed(String name) {
        def tag = environmentTag(name)
        return tag.size() == 1 && tag.displayed
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
        environmentTagRemoveButton(name).click()
    }
}
