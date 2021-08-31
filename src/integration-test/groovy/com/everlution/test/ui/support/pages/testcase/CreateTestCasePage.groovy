package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.TestStepTableModule

class CreateTestCasePage extends BasePage {
    static url = "/testCase/create"
    static at = { title == "Create TestCase" }

    static content = {
        createButton { $("#create") }
        descriptionInput { $("#description") }
        executionMethodOptions { $("#executionMethod>option") }
        executionMethodSelect { $("#executionMethod")}
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link") }
        nameInput { $("#name") }
        testStepTable { module TestStepTableModule }
        typeOptions { $("#type>option") }
        typeSelect { $("#type") }
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
     * adds a generic test case
     */
    void createTestCase() {
        nameInput = "fake test case"
        descriptionInput = "fake description"
        testStepTable.addTestStep("step action", "step result")
        createButton.click()
    }

    /**
     * fills in all information for the create form
     * but does not submit
     */
    void completeCreateForm() {
        nameInput = "fake test case"
        descriptionInput = "fake description"
        testStepTable.addTestStep("step action", "step result")
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
