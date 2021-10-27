package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class ShowProjectPage extends BasePage {
    static url = "/project/show"
    static at = { title == "Show Project" }

    static content = {
        areasList { $("#areas") }
        createLink(required: false) { $("[data-test-id=show-create-link]") }
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        homeLink { $("[data-test-id=show-home-link]") }
        listLink { $("[data-test-id=show-list-link]") }
        fieldLabels { $("ol.property-list>li>span") }
        statusMessage { $("div.message") }
    }

    /**
     * clicks the delete link
     */
    void deleteProject() {
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
     * clicks the new link
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
     * determines if an area is displayed
     * @param name - name of the area to check
     */
    boolean isAreaDisplayed(String name) {
        return areasList.find("div")*.text().contains(name)
    }
}
