package com.everlution.test.ui.support.pages.testiteration

import com.everlution.test.ui.support.pages.common.BasePage
import geb.module.Select

class ExecuteTestIterationPage extends BasePage {
    static url = "/testIteration/execute"
    static at = { title == "Execute Test" }

    static content = {
        fieldLabels { $("ol.property-list>li>span") }
        resultOptions { $("#result>option") }
        statusMessage { $("div.message") }
        testCaseLink { $("#testCase") }
        testCycleLink { $("div[aria-labelledby=testCycle-label]>a") }
        updateButton { $("[data-test-id=edit-update-button]") }
    }

    Select resultSelect() {
        $("#result").module(Select)
    }

    /**
     * Gets the labels for all fields displayed on the page
     * @return - a list of field names
     */
    List<String> getFields() {
        return fieldLabels*.text()
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

    /**
     * Sets the result and submits form
     */
    void setResult(String result) {
        resultSelect().selected = result
        updateButton.click()
    }
}
