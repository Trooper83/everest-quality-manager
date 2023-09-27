package com.everlution.test.ui.support.pages.common

class CreatePage extends BasePage {
    static content = {
        createButton { $("#create") }
        errorsMessage { $(".alert-danger") }
    }

    /**
     * submits the form
     */
    void submit() {
        scrollToBottom()
        createButton.click()
    }
}
