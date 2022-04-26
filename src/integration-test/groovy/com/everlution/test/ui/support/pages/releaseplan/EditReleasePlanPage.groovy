package com.everlution.test.ui.support.pages.releaseplan

import com.everlution.test.ui.support.pages.common.EditPage

class EditReleasePlanPage extends EditPage {
    static at = { title == "Edit ReleasePlan" }

    static String convertToPath(Long projectId, Long id) {
        "/project/${projectId}/releasePlan/edit/${id}"
    }

    static content = {
        nameInput { $("#name") }
        projectNameField { $("[data-test-id=edit-project-name]") }
    }

    /**
     * edits a plan with the supplied data
     */
    void editReleasePlan(String name) {
        nameInput = name
        updateButton.click()
    }
}
