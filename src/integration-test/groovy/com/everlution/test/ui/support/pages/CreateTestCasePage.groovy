package com.everlution.test.ui.support.pages

class CreateTestCasePage extends BasePage {
    static url = "/testCase/create"
    static at = { title == "Create TestCase" }

    static content = {
        errorText { $("div.errors") }
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link") }
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
