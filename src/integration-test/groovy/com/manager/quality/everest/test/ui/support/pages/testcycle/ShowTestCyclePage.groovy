package com.manager.quality.everest.test.ui.support.pages.testcycle

import com.manager.quality.everest.test.ui.support.pages.common.ShowPage
import com.manager.quality.everest.test.ui.support.pages.modules.TableModule
import geb.module.MultipleSelect

class ShowTestCyclePage extends ShowPage {
    static at = { title == "TestCycle Details" }

    static String convertToPath(Long projectId, Long id) {
        "/project/${projectId}/testCycle/show/${id}"
    }

    static content = {
        addTestsButton(required: false) { $("#addTestsBtn") }
        addTestsModal (required: false) { $("#testsModal") }
        addTestsModalCancelButton { $("[data-test-id=modal-cancel-button]") }
        addTestsModalCloseButton { $("[data-test-id=modal-close-button]") }
        addTestsModalSubmitButton { $("[data-test-id=modal-submit-button]") }
        progressBar { $(".progress-stacked") }
        releasePlanLink { $("#releasePlan") }
        testGroupsOptions { $("#testGroups>option") }
        testsTable { module TableModule }
    }

    MultipleSelect testGroupsSelect() {
        $("#testGroups").module(MultipleSelect)
    }

    /**
     * adds tests by test group
     */
    void addTestsByGroup() {
        addTestsButton.click()
        waitFor {
            addTestsModal.displayed
        }
        testGroupsSelect().selected = [testGroupsOptions*.text().first()]
        addTestsModalSubmitButton.click()
        waitFor {
            !addTestsModal.displayed
        }
    }

    /**
     * adds tests by test group
     */
    void addTestsByGroup(String group) {
        addTestsButton.click()
        waitFor {
            addTestsModal.displayed
        }
        testGroupsSelect().selected = [group]
        addTestsModalSubmitButton.click()
        waitFor {
            !addTestsModal.displayed
        }
    }

    /**
     * cancels the add tests modal
     */
    void cancelAddTestsModal() {
        sleep(2000) //need to slow test down
        addTestsModalCancelButton.click()
    }
    /**
     * closes the add tests modal
     */
    void closeAddTestsModal() {
        sleep(2000) //need to slow test down
        addTestsModalCloseButton.click()
        if(addTestsModalCloseButton.displayed) {
            addTestsModalCloseButton.click()
        }
    }

    /**
     * clicks add tests button
     */
    void displayAddTestsModal() {
        addTestsButton.click()
        waitFor {
            addTestsModal.displayed
        }
    }

    /**
     * clicks the release plan link
     */
    void goToReleasePlan() {
        releasePlanLink.click()
    }
}
