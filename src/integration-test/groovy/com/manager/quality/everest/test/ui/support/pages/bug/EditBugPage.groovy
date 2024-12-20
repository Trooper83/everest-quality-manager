package com.manager.quality.everest.test.ui.support.pages.bug


import com.manager.quality.everest.test.ui.support.pages.common.EditPage
import com.manager.quality.everest.test.ui.support.pages.modules.StepTableModule
import geb.module.MultipleSelect
import geb.module.Select

class EditBugPage extends EditPage {

    static at = { title == "Edit Bug" }
    static convertToPath(Long projectId, Long bugId) {
        "/project/${projectId}/bug/edit/${bugId}"
    }
    static content = {
        actualInput { $("#actual") }
        areaOptions { $("#area>option") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        expectedInput { $("#expected") }
        homeLink { $("[data-test-id=edit-home-link]") }
        listLink { $("[data-test-id=edit-list-link]") }
        nameInput { $("#name") }
        notesInput { $("#notes") }
        platformOptions { $("#platform>option") }
        projectNameField { $("[data-test-id=edit-project-name]") }
        statusOptions { $("#status>option") }
        stepRemovedInput { $("input[data-test-id='step-removed-input']") }
        stepsTable { module StepTableModule }
    }

    /**
     * select element strongly typed for convenience in tests
     */
    Select areaSelect() {
        $("#area").module(Select)
    }

    MultipleSelect environmentsSelect() {
        $("#environments").module(MultipleSelect)
    }

    Select platformSelect() {
        $("#platform").module(Select)
    }

    Select statusSelect() {
        $("#status").module(Select)
    }

    /**
     * edits a bug with the supplied data
     */
    void editBug(String name, String description, String area, List<String> environments, String platform, String notes,
                 String expected, String actual) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        platformSelect().selected = platform
        environmentsSelect().selected = environments
        notesInput = notes
        statusSelect().selected = "Closed"
        expectedInput = expected
        actualInput = actual
        scrollToBottom()
        updateButton.click()
    }
}
