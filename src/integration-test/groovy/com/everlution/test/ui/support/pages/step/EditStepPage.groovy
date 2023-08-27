package com.everlution.test.ui.support.pages.step

import com.everlution.test.ui.support.pages.common.EditPage

class EditStepPage extends EditPage {
    static url = "/step/edit"
    static at = { title == "Edit Step" }

    static content = {
        actionInput { $("#act") }
        resultInput { $("#result") }
        nameInput { $("#name") }
    }

    /**
     * edits a step with the passed in data
     */
    void editStep(String action, String name, String result) {
        nameInput = name
        actionInput = action
        resultInput = result
        updateButton.click()
    }
}
