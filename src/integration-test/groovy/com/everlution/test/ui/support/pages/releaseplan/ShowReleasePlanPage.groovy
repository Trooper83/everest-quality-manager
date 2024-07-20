package com.everlution.test.ui.support.pages.releaseplan

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.pages.common.ShowPage
import geb.module.Select

class ShowReleasePlanPage extends ShowPage {
    static at = { title == "ReleasePlan Details" }

    static String convertToPath(Long projectId, Long id) {
        "project/${projectId}/releasePlan/show/${id}"
    }

    static content = {
        addTestCycleButton(required: false) { $("#addTestCycleBtn") }
        nameValue { $("#name") }
        notesValue { $("#notes") }
        plannedDateValue { $("#plannedDate") }
        releaseDateValue { $("#releaseDate") }
        statusValue { $("#status") }
        testCycleButtons { $("#testCycles button") }
        testCyclesContent { $("#testCycles [data-test-id=testCycle-content]") }
        testCycleModal { $("#testCycleModal") }
        testCycleModalCancelButton { testCycleModal.find("[data-test-id=modal-cancel-button]") }
        testCycleModalCloseButton { testCycleModal.find("[data-test-id=modal-close-button]") }
        testCycleModalCreateButton { testCycleModal.find("[data-test-id=modal-submit-button]") }
        testCycleModalEnvironOptions { testCycleModal.find("[id='testCycle.environ]' > option") }
        testCycleModalNameInput { $(testCycleModal.find("[id='testCycle.name']")) }
        testCycleModalPlatformOptions { testCycleModal.find("[id='testCycle.platform'] > option") }
        testCycleModalSubmitButton { testCycleModal.find("[data-test-id=modal-submit-button]") }
        testCycleProgressBars { $(".progress-stacked") }
        testCycleViewLink { $("#testCycles [data-test-id=view-test-cycle-link]") }
    }

    Select testCycleModalEnvironSelect() {
        testCycleModal.find("[id='testCycle.environ']").module(Select)
    }

    Select testCycleModalPlatformSelect() {
        testCycleModal.find("[id='testCycle.platform']").module(Select)
    }

    /**
     * determines if the required field indication (asterisk) is
     * displayed for the supplied fields
     * @param fields - list of fields
     * @return - true if all fields have the indicator, false if at least one does not
     */
    boolean areRequiredFieldsDisplayedForTestCycle(List<String> fields) {
        for(field in fields) {
            def sel = $("#testCycleModal label[for=${field}]>span.required-indicator")
            if (!sel.displayed) {
                return false
            }
        }
        return true
    }

    /**
     * creates a generic test cycle
     */
    void createTestCycle() {
        addTestCycleButton.click()
        waitFor {
            testCycleModal.displayed
            testCycleModalNameInput.displayed
        }
        testCycleModalNameInput << DataFactory.testCycle().name
        testCycleModalPlatformSelect().selected = ""
        testCycleModalCreateButton.click()
        waitFor {
            !testCycleModal.displayed
        }
    }

    /**
     * creates a test cycle
     */
    void createTestCycle(String name, String environ, String platform) {
        addTestCycleButton.click()
        waitFor {
            testCycleModal.displayed
        }
        testCycleModalNameInput << name
        testCycleModalEnvironSelect().selected = environ
        testCycleModalPlatformSelect().selected = platform
        testCycleModalCreateButton.click()
    }

    /**
     * cancels the test cycle modal
     */
    void cancelTestCycleModal() {
        waitFor {
            testCycleModalSubmitButton.displayed
            testCycleModalCancelButton.displayed
        }
        testCycleModalCancelButton.click()
        waitFor {
            !testCycleModal.displayed
        }
    }
    /**
     * closes the test cycle modal
     */
    void closeTestCycleModal() {
        sleep(2000) //need to slow test down
        testCycleModalCloseButton.click()
        waitFor {
            !testCycleModal.displayed
        }
    }

    /**
     * completes the test cycle form without submitting
     */
    void completeTestCycleForm() {
        waitFor {
            testCycleModalCloseButton.displayed
        }
        testCycleModalNameInput << "name"
        testCycleModalPlatformSelect().selected = "Web"
        testCycleModalEnvironSelect().selected = "1"
    }

    /**
     * clicks the delete link
     */
    void deletePlan() {
        withConfirm(true) { deleteLink.click() }
    }

    /**
     * displays the add test cycle modal
     */
    void displayAddTestCycleModal() {
        addTestCycleButton.click()
        waitFor {
            testCycleModal.displayed
        }
    }

    /**
     * clicks the view link for a test cycle
     * @param index
     */
    void goToTestCycle(int index) {
        scrollToBottom()
        testCycleButtons[index].click()
        waitFor { testCycleViewLink[index].displayed }
        scrollToBottom()
        testCycleViewLink[index].click()
    }

    /**
     * determines if a test cycle is present
     */
    boolean isTestCyclePresent(String name) {
        return testCycleButtons*.text().contains(name)
    }
}
