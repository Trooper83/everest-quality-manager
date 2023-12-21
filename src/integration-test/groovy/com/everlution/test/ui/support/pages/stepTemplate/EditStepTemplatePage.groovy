package com.everlution.test.ui.support.pages.stepTemplate

import com.everlution.test.ui.support.pages.common.EditPage
import com.everlution.test.ui.support.pages.modules.LinkModule

class EditStepTemplatePage extends EditPage {

    static at = { title == "Edit Step Template" }

    static String convertToPath(Long projectId, Long stepId) {
        "/project/${projectId}/stepTemplate/edit/${stepId}"
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
    void editStepTemplate(String action, String name, String result) {
        nameInput = name
        actionInput = action
        resultInput = result
        scrollToBottom()
        updateButton.click()
    }
}
