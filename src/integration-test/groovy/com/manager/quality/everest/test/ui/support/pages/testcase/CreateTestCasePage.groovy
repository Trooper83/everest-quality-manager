package com.manager.quality.everest.test.ui.support.pages.testcase

import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.test.ui.support.pages.common.CreatePage
import com.manager.quality.everest.test.ui.support.pages.modules.SideBarModule
import com.manager.quality.everest.test.ui.support.pages.modules.StepTableModule
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
        sideBar { module SideBarModule }
        testGroupsOptions { $("#testGroups>option") }
        testStepTable { module StepTableModule }
        typeOptions { $("#type>option") }
        verifyInput { $("#verify") }
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
        testStepTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(3),
                faker.lorem().sentence(7))
        createButton.click()
    }

    /**
     * adds a generic test case with a builder step
     */
    void createBuilderTestCase() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        scrollToBottom()
        testStepTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(3),
                faker.lorem().sentence(7))
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
        steps.eachWithIndex { it, idx ->
            if (idx == 0) {
                testStepTable.addBuilderStep(it)
            } else {
                testStepTable.addBuilderStep(it, false)
            }
        }
        testStepTable.appendBuilderSteps()
        scrollToBottom()
        createButton.click()
    }

    /**
     * creates a test case with the supplied data
     */
    void createFreeFormTestCase(String name, String description, String area, List<String> environments,
                                List<String> testGroups, String method, String type, String platform, List<Step> steps,
                                String verify) {
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
            testStepTable.addStep(it.act, it.data, it.result)
        }
        verifyInput = verify
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
        testStepTable.addStep("step action", "step data","step result")
    }
}
