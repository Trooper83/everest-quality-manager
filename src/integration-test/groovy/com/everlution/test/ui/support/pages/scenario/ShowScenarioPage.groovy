package com.everlution.test.ui.support.pages.scenario

import com.everlution.test.ui.support.pages.common.BasePage

class ShowScenarioPage extends BasePage {
    static url = "/scenario/show"
    static at = { title == "Show Scenario" }

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
        gherkinTextArea { $("#gherkin") }
        homeLink { $("[data-test-id=show-home-link]") }
        listLink { $("[data-test-id=show-list-link]") }
        nameValue { $("#name") }
        projectValue { $("#project") }
        statusMessage { $("div.message") }
        typeValue { $ ("#type") }
    }

    /**
     * clicks the delete link
     */
    void deleteScenario() {
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
     * clicks the new scenario link
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
