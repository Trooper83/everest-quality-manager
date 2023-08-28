package com.everlution.test.ui.support.pages.common

class EditPage extends BasePage {
    static content = {
        errorsMessage { $(".alert-danger")}
        updateButton { $("#update") }
    }

    /**
     * clicks the update button
     */
    void edit() {
        scrollToBottom()
        updateButton.click()
    }
}
