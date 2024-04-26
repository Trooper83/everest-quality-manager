package com.everlution.test.ui.support.pages.testiteration

import com.everlution.test.ui.support.pages.common.BasePage
import geb.module.Select

class ExecuteTestIterationPage extends BasePage {
    static url = "/testIteration/execute"
    static at = { title == "Execute Test" }

    static content = {
        notesTextArea { $("[data-test-id=notes]") }
        resultOptions { $("[data-test-id=result]>option") }
        statusMessage { $(".alert-primary") }
        testCaseLink { $("#testCase") }
        testCycleLink { $("#testCycle") }
        completeButton { $("#complete") }
    }

    Select resultSelect() {
        $("[data-test-id=result]").module(Select)
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
    void setResult(String result, String notes) {
        resultSelect().selected = result
        notesTextArea << notes
        scrollToBottom()
        completeButton.click()
    }
}
