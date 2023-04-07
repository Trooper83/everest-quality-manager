package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class CreateProjectPage extends BasePage {
    static url = "/project/create"
    static at = { title == "Create Project" }

    static content = {
        addAreaButton { $("#btnAddArea") }
        areaInput { $("#area") }
        areaItem { name -> $("#areaRow li", name: name) }
        areaTag(required: false) { text -> $("#areaRow p", text: text) }
        areaTagInput { text -> areaItem(text).find("[data-test-id='tag-input']") }
        areaTagRemoveButton { text -> areaTag(text).find("svg") }
        areaTags(required: false) { $("#areaRow p") }
        codeInput { $("#code") }
        createButton { $("#create") }
        addEnvironmentButton { $("#btnAddEnv") }
        environmentItem { name -> $("#environmentRow li", name: name) }
        environmentInput { $("#environment") }
        environmentTag(required: false) { text -> $("#environmentRow p", text: text) }
        environmentTagInput { text -> environmentItem(text).find("[data-test-id='tag-input']") }
        environmentTagRemoveButton { text -> environmentTag(text).find("svg") }
        environmentTags(required: false) { $("#environmentRow p") }
        errors { $("ul.alert-danger>li") }
        nameInput { $("#name") }
        tooltip(wait: true) { $("div[role=tooltip]") }
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
        areaTagRemoveButton(name).click()
    }

    /**
     * removes an environment tag
     * @param name - name of the tag to remove
     */
    void removeEnvironmentTag(String name) {
        environmentTagRemoveButton(name).click()
    }

    /**
     * clicks the create button
     */
    void submitForm() {
        createButton.click()
    }
}
