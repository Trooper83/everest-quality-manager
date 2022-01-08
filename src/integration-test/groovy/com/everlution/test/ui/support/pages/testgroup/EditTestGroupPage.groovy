package com.everlution.test.ui.support.pages.testgroup

import com.everlution.test.ui.support.pages.common.EditPage

class EditTestGroupPage extends EditPage {
    static url = "/testGroup/edit"
    static at = { title == "Edit TestGroup" }

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
