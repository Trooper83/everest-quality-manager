package com.everlution.test.ui.support.pages.releaseplan

import com.everlution.test.ui.support.pages.common.BasePage

class ShowReleasePlanPage extends BasePage {
    static url = "/releasePlan/show"
    static at = { title == "Show ReleasePlan" }

    static content = {
        createLink(required: false) { $("[data-test-id=show-create-link]") }
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        fieldLabels { $("ol.property-list>li>span") }
        homeLink { $("[data-test-id=show-home-link]") }
        listLink { $("[data-test-id=show-list-link]") }
        nameValue { $("#name") }
        projectValue { $("#project") }
        statusMessage { $("div.message") }
    }

    /**
     * clicks the delete link
     */
    void deletePlan() {
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
}
