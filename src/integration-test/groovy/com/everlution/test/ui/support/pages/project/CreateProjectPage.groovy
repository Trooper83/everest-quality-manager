package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class CreateProjectPage extends BasePage {
    static url = "/project/create"
    static at = { title == "Create Project" }

    static content = {
        codeInput { $("#code") }
        createButton { $("#create") }
        errors { $("ul.errors>li") }
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link]") }
        nameInput { $("#name") }
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
     * @param name
     * @param code
     */
    void completeCreateForm(String name, String code) {
        nameInput << name
        codeInput << code
    }

    /**
     * fills in all fields for the create form and submits
     * @param name
     * @param code
     */
    void createProject(String name, String code) {
        this.completeCreateForm(name, code)
        createButton.click()
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
