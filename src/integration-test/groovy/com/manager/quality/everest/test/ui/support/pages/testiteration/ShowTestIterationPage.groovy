package com.manager.quality.everest.test.ui.support.pages.testiteration

import com.manager.quality.everest.test.ui.support.pages.common.ShowPage
import com.manager.quality.everest.test.ui.support.pages.modules.TableModule

class ShowTestIterationPage extends ShowPage {
    static url = "/testIteration/show"
    static at = { title == "TestIteration Details" }

    static content = {
        resultsTable { module TableModule }
        testCaseLink { $("#testCase") }
        testCycleLink { $("#testCycle") }
    }

    /**
     * clicks test case link
     */
    void goToTestCase() {
        testCaseLink.click()
    }

    /**
     * clicks test cycle link
     */
    void goToTestCycle() {
        testCycleLink.click()
    }
}
