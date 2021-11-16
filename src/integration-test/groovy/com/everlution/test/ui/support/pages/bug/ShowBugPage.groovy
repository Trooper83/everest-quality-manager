package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.StepTableModule

class ShowBugPage extends BasePage {
    static url = "/bug/show"
    static at = { title == "Show Bug" }

    static content = {
        areaValue { $("#area") }
        createLink(required: false) { $("[data-test-id=show-create-link]") }
        creatorValue { $("#creator") }
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        descriptionValue { $("#description") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        environmentsList { $("#environments") }
        fieldLabels { $("ol.property-list>li>span") }
        homeLink { $("[data-test-id=show-home-link]") }
        listLink { $("[data-test-id=show-list-link]") }
        nameValue { $("#name") }
        projectValue { $("#project") }
        statusMessage { $("div.message") }
        stepsTable { module StepTableModule }
    }

    /**
     * clicks the delete link
     */
    void deleteBug() {
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
     * determines if an area is displayed
     * @param names - name of the area to check
     */
    boolean areEnvironmentsDisplayed(List<String> names) {
        return environmentsList.find("div")*.text().containsAll(names)
    }
}
