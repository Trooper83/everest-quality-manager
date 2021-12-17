package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.StepTableModule

class ShowTestCasePage extends BasePage {
    static url = "/testCase/show"
    static at = { title == "Show TestCase" }

    static content = {
        areaValue { $("#area") }
        createLink(required: false) { $("[data-test-id=show-create-link]") }
        creatorValue { $("#creator") }
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        descriptionValue { $("#description") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        environmentsList { $("#environments") }
        executionMethodValue { $("#executionMethod") }
        fieldLabels { $("ol.property-list>li>span") }
        homeLink { $("[data-test-id=show-home-link]") }
        listLink { $("[data-test-id=show-list-link]") }
        nameValue { $("#name") }
        platformValue { $("#platform") }
        projectValue { $("#project") }
        statusMessage { $("div.message") }
        testStepTable { module StepTableModule }
        typeValue { $ ("#type") }
    }

    /**
     * clicks the delete link
     */
    void deleteTestCase() {
        withConfirm(true) { deleteLink.click() }
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

    /**
     * determines if an environment is displayed
     * @param names - name of the environment to check
     */
    boolean areEnvironmentsDisplayed(List<String> names) {
        return environmentsList.find("div")*.text().containsAll(names)
    }
}
