package com.everlution.test.ui.support.pages.step

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.pages.common.CreatePage

class CreateStepPage extends CreatePage {

    static url = "/step/create"
    static at = { title == "Create Step" }

    static content = {
        actionInput { $("#act") }
        resultInput { $("#result") }
        nameInput { $("#name") }
    }

    /**
     * creates a generic step
     */
    void createStep() {
        def step = DataFactory.step()
        nameInput = step.name
        actionInput = step.action
        resultInput = step.result
        createButton.click()
    }

    /**
     * creates a step with the passed in data
     */
    void createStep(String action, String name, String result) {
        nameInput = name
        actionInput = action
        resultInput = result
        createButton.click()
    }
}
