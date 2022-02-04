package com.everlution.test.ui.support.pages.testcycle

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.pages.common.CreatePage
import geb.module.Select

class CreateTestCyclePage extends CreatePage {
    static url = "/testCycle/create"
    static at = { title == "Create TestCycle" }

    static content = {
        cancelButton { $("#cancel") }
        environOptions { $("#environ>option") }
        nameInput { $("#name") }
        platformOptions { $("#platform>option") }
    }

    Select environSelect() {
        $("#environ").module(Select)
    }

    Select platformSelect() {
        $("#platform").module(Select)
    }

    /**
     * creates a generic test cycle
     */
    void createTestCycle() {
        nameInput << DataFactory.testCycle().name
        environSelect().selected = "1"
        platformSelect().selected = "Web"
        createButton.click()
    }

    /**
     * creates a test cycle
     */
    void createTestCycle(String name, String environ, String platform) {
        nameInput << name
        environSelect().selected = environ
        platformSelect().selected = platform
        createButton.click()
    }

    /**
     * clicks the cancel button
     */
    void goToReleasePlan() {
        cancelButton.click()
    }
}
