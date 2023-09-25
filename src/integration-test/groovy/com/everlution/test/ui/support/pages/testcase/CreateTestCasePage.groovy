package com.everlution.test.ui.support.pages.testcase

import com.everlution.Step
import com.everlution.test.ui.support.pages.common.CreatePage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import com.github.javafaker.Faker
import geb.module.MultipleSelect
import geb.module.Select

class CreateTestCasePage extends CreatePage {

    static at = { title == "Create TestCase" }
    static String convertToPath(Long projectId) {
        "/project/${projectId}/testCase/create"
    }

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
    void createFreeFormTestCase() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        scrollToBottom()
        testStepTable.selectStepsTab('free-form')
        testStepTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
        createButton.click()
    }

    /**
     * creates a test case with the supplied data
     */
    void createBuilderTestCase(String name, String description, String area, List<String> environments,
                                List<String> testGroups, String method, String type, String platform, List<String> steps) {
        nameInput = name
        descriptionInput = description
        executionMethodSelect().selected = method
        platformSelect().selected = platform
        typeSelect().selected = type
        areaSelect().selected = area
        environmentsSelect().selected = environments
        testGroupsSelect().selected = testGroups
        scrollToBottom()
        steps.each { it ->
            testStepTable.addBuilderStep(it)
        }
        scrollToBottom()
        createButton.click()
    }

    /**
     * creates a test case with the supplied data
     */
    void createFreeFormTestCase(String name, String description, String area, List<String> environments,
                                List<String> testGroups, String method, String type, String platform, List<Step> steps) {
        nameInput = name
        descriptionInput = description
        executionMethodSelect().selected = method
        platformSelect().selected = platform
        typeSelect().selected = type
        areaSelect().selected = area
        environmentsSelect().selected = environments
        testGroupsSelect().selected = testGroups
        scrollToBottom()
        testStepTable.selectStepsTab('free-form')
        steps.each { it ->
            testStepTable.addStep(it.act, it.result)
        }
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
        testStepTable.selectStepsTab('free-form')
        testStepTable.addStep("step action", "step result")
    }
}
