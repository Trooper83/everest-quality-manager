package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.CreatePage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import com.github.javafaker.Faker
import geb.module.MultipleSelect
import geb.module.Select

class CreateTestCasePage extends CreatePage {
    static url = "/testCase/create"
    static at = { title == "Create TestCase" }

    static content = {
        areaOptions { $("#area>option") }
        createButton { $("#create") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        errorsMessage { $(".alert-danger") }
        executionMethodOptions { $("#executionMethod>option") }
        nameInput { $("#name") }
        platformOptions { $("#platform>option") }
        testGroupsOptions { $("#testGroups>option") }
        testStepTable { module StepTableModule }
        typeOptions { $("#type>option") }
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
     * adds a generic test case
     */
    void createTestCase() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        scrollToBottom()
        testStepTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
        createButton.click()
    }

    /**
     * creates a test case with the supplied data
     */
    void createTestCase(String name, String description, String area, List<String> environments,
                        List<String> testGroups, String method, String type, String platform) {
        nameInput = name
        descriptionInput = description
        executionMethodSelect().selected = method
        platformSelect().selected = platform
        typeSelect().selected = type
        areaSelect().selected = area
        environmentsSelect().selected = environments
        testGroupsSelect().selected = testGroups
        scrollToBottom()
        createButton.click()
    }

    /**
     * fills in all information for the create form
     * but does not submit
     */
    void completeCreateForm() {
        nameInput = "fake test case"
        descriptionInput = "fake description"
        scrollToBottom()
        testStepTable.addStep("step action", "step result")
    }
}
