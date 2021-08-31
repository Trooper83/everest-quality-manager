package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.TestStepTableModule

class ShowTestCasePage extends BasePage {
    static url = "/testCase/show"
    static at = { title == "Show TestCase" }

    static content = {
        createLink(required: false) { $("[data-test-id=show-create-link]") }
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        homeLink { $("[data-test-id=show-home-link]") }
        listLink { $("[data-test-id=show-list-link]") }
        fieldLabels { $("ol.property-list>li>span") }
        statusMessage { $("div.message") }
        testStepTable { module TestStepTableModule }
    }

    /**
     * Gets the labels for all fields displayed on the page
     * @return - a list of field names
     */
    List<String> getFields() {
        return fieldLabels*.text()
    }

    /**
     * clicks the new test case link
     */
    void goToCreate() {
        createLink.click()
    }

    /**
     * clicks the edit link
     */
    void goToEdit() {
        editLink.click()
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
