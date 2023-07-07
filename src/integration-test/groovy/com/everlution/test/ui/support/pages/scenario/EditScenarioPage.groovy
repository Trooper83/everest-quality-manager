package com.everlution.test.ui.support.pages.scenario

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.common.EditPage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import geb.module.MultipleSelect
import geb.module.Select

class EditScenarioPage extends EditPage {
    static url = "/scenario/edit"
    static at = { title == "Edit Scenario" }

    static content = {
        areaOptions { $("#area>option") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        executionMethodOptions { $("#executionMethod>option") }
        fieldLabels { $("fieldset label") }
        gherkinTextArea { $("#gherkin") }
        nameInput { $("#name") }
        platformOptions { $("#platform>option") }
        projectNameField { $("[data-test-id=edit-project-name]") }
        stepRemovedInput { $("input[data-test-id='step-removed-input']") }
        testStepTable { module StepTableModule }
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

    Select typeSelect() {
        $("#type").module(Select)
    }

    /**
     * clicks the update button
     */
    void editScenario() {
        //Need to scroll button into view
        Thread.sleep(500)
        driver.executeScript("window.scrollBy(0,document.body.scrollHeight)")
        Thread.sleep(500)
        updateButton.click()
    }

    /**
     * edits a scenario with the supplied data
     */
    void editScenario(String name, String description, String gherkin, String area, List<String> environment,
                      String method, String type, String platform) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        environmentsSelect().selected = environment
        executionMethodSelect().selected = method
        typeSelect().selected = type
        platformSelect().selected = platform
        gherkinTextArea = gherkin
        //Need to scroll button into view
        Thread.sleep(500)
        driver.executeScript("window.scrollBy(0,document.body.scrollHeight)")
        Thread.sleep(500)
        updateButton.click()
    }
}
