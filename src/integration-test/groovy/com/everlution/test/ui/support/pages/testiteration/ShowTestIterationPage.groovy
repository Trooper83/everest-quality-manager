package com.everlution.test.ui.support.pages.testiteration

import com.everlution.test.ui.support.pages.common.ShowPage

class ShowTestIterationPage extends ShowPage {
    static url = "/testIteration/show"
    static at = { title == "Show TestIteration" }

    static content = {
        testCaseLink { $("#testCase") }
    }

    /**
     * clicks test case link
     */
    void goToTestCase() {
        testCaseLink.click()
    }
}
