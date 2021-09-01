package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.TableModule

class ListTestCasePage extends BasePage {
    static url = "/testCase/index"
    static at = { title == "TestCase List" }

    static content = {
        createTestCaseLink(required: false) { $("[data-test-id=index-create-button]") }
        homeLink { $("[data-test-id=index-home-link]") }
        statusMessage { $("div.message") }
        testCaseTable { module TableModule }
    }

    /**
     * clicks the new test case button
     */
    void goToCreateTestCase() {
        createTestCaseLink.click()
    }

    /**
     * clicks the home link
     */
    void goToHome() {
        homeLink.click()
    }
}
