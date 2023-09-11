package com.everlution.test.ui.support.pages.step

import com.everlution.test.ui.support.pages.common.EditPage
import com.everlution.test.ui.support.pages.modules.LinkModule

class EditStepPage extends EditPage {

    static at = { title == "Edit Step" }

    static String convertToPath(Long projectId, Long stepId) {
        "/project/${projectId}/step/edit/${stepId}"
    }

    static content = {
        actionInput { $("#act") }
        linkModule { module LinkModule }
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
        scrollToBottom()
        updateButton.click()
    }
}
