package com.everlution.test.ui.support.pages

import com.everlution.test.ui.support.pages.modules.TableModule
import geb.error.RequiredPageContentNotPresent

class TestCaseIndexPage extends BasePage {
    static url = "/testCase/index"
    static at = { title == "TestCase List" }

    static content = {
        createTestCaseButton { $("[data-test-id=index-create-button]") }
        testCaseTable { module TableModule }
    }

    /**
     * determines if button is displayed
     * @return - true if displayed, false if not
     */
    boolean isCreateButtonDisplayed() {
        try {
            createTestCaseButton.displayed
        } catch(RequiredPageContentNotPresent ignored) {
            return false
        }
        return true
    }
}
