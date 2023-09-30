package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.EditPage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import geb.module.MultipleSelect
import geb.module.Select

class EditTestCasePage extends EditPage {

    static at = { title == "Edit TestCase" }
    static String convertToPath(Long projectId, Long testCaseId) {
        "/project/${projectId}/testCase/edit/${testCaseId}"
    }

    static content = {
        areaOptions { $("#area>option") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        executionMethodOptions { $("#executionMethod>option") }
        nameInput { $("#name") }
        platformOptions { $("#platform>option") }
        projectNameField { $("[data-test-id=edit-project-name]") }
        stepRemovedInput { $("input[data-test-id='step-removed-input']") }
        stepsTable { module StepTableModule }
        testGroupsOptions { $("#testGroups>option") }
        typeOptions { $("#type>option") }
        updateButton { $("[data-test-id=edit-update-button]")}
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

    Select executionMethodSelect() {
        $("#executionMethod").module(Select)
    }

    Select platformSelect() {
        $("#platform").module(Select)
    }

    MultipleSelect testGroupsSelect() {
        $("#testGroups").module(MultipleSelect)
    }

    Select typeSelect() {
        $("#type").module(Select)
    }

    /**
     * fills in all data but does not submit the form
     */
    void completeEditForm(String name, String description, String area, List<String> environments, String method,
                          String type, String platform, List<String> testGroups) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        environmentsSelect().selected = environments
        testGroupsSelect().selected = testGroups
        executionMethodSelect().selected = method
        platformSelect().selected = platform
        typeSelect().selected = type
    }

    /**
     * clicks the update button
     */
    void editTestCase() {
        scrollToBottom()
        updateButton.click()
    }

    /**
     * edits a test case with the supplied data
     */
    void editTestCase(String name, String description, String area, List<String> environments, String method,
                      String type, String platform, List<String> testGroups) {
        completeEditForm(name, description, area, environments, method, type, platform, testGroups)
        scrollToBottom()
        updateButton.click()
    }
}
