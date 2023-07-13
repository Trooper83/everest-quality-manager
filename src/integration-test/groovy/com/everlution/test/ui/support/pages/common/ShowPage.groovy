package com.everlution.test.ui.support.pages.common

class ShowPage extends BasePage {
    static content = {
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        errorsMessage { $(".alert-danger") }
        statusMessage { $("div.alert-primary") }
    }

    /**
     * clicks the delete link
     */
    void delete() {
        withConfirm(true) { deleteLink.click() }
    }

    /**
     * clicks the edit link
     */
    void goToEdit() {
        editLink.click()
    }
}
