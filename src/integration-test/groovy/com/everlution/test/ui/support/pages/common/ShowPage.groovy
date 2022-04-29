package com.everlution.test.ui.support.pages.common

import com.everlution.test.ui.support.pages.modules.ProjectNavModule

class ShowPage extends BasePage {
    static content = {
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        errorsMessage { $(".errors") }
        fieldLabels { $("ol.property-list>li>span") }
        projectNavButtons { module ProjectNavModule }
        statusMessage { $("div.message") }
    }

    /**
     * clicks the delete link
     */
    void delete() {
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
     * clicks the edit link
     */
    void goToEdit() {
        editLink.click()
    }
}
