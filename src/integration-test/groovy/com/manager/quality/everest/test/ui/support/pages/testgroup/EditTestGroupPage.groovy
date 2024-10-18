package com.manager.quality.everest.test.ui.support.pages.testgroup

import com.manager.quality.everest.test.ui.support.pages.common.EditPage

class EditTestGroupPage extends EditPage {
    static at = { title == "Edit TestGroup" }

    static String convertToPath(Long projectId, Long id) {
        "/project/${projectId}/testGroup/edit/${id}"
    }

    static content = {
        nameInput { $("#name") }
        projectNameField { $("[data-test-id=edit-project-name]") }
    }

    /**
     * edits a test group
     * @param name
     */
    void editTestGroup(String name) {
        nameInput = name
        updateButton.click()
    }
}
