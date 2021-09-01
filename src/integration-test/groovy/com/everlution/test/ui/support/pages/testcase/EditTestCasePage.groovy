package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.TestStepTableModule

class EditTestCasePage extends BasePage {
    static url = "/testCase/edit"
    static at = { title == "Edit TestCase" }

    static content = {
        descriptionInput { $("#description") }
        executionMethodOptions { $("#executionMethod>option") }
        executionMethodSelect { $("#executionMethod") }
        homeLink { $("[data-test-id=edit-home-link]") }
        listLink { $("[data-test-id=edit-list-link]") }
        nameInput { $("#name") }
        testStepTable { module TestStepTableModule }
        typeOptions { $("#type>option") }
        typeSelect { $("#type") }
        updateButton { $("[data-test-id=edit-update-button]")}
    }

    /**
     * clicks the update button
     */
    void editTestCase() {
        updateButton.click()
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
