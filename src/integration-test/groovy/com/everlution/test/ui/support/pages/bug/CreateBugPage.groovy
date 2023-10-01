package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.CreatePage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import com.github.javafaker.Faker
import geb.module.MultipleSelect
import geb.module.Select

class CreateBugPage extends CreatePage {

    static at = { title == "Create Bug" }
    static convertToPath(Long projectId) {
        "/project/${projectId}/bug/create"
    }

    static content = {
        actualInput { $("#actual") }
        areaOptions { $("#area>option") }
        createButton { $("#create") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        errorsMessage { $(".alert-danger") }
        expectedInput { $("#expected") }
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link]") }
        nameInput { $("#name") }
        platformOptions { $("#platform>option") }
        stepsTable { module StepTableModule }
    }

    /**
     * select elements strongly typed for convenience in tests
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

    /**
     * creates a bug with the supplied data
     */
    void createBuilderBug(String name, String description, String area, List<String> environment,
                           String platform, String stepName, String actual, String expected) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        platformSelect().selected = platform
        environmentsSelect().selected = environment
        scrollToBottom()
        stepsTable.addBuilderStep(stepName)
        expectedInput = expected
        actualInput = actual
        createButton.click()
    }

    /**
     * adds a generic bug
     */
    void createFreeFormBug() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        actualInput = faker.beer().name()
        expectedInput = faker.beer().name()
        scrollToBottom()
        stepsTable.selectStepsTab('free-form')
        stepsTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
        createButton.click()
    }

    /**
     * creates a bug with the supplied data
     */
    void createFreeFormBug(String name, String description, String area, List<String> environment,
                           String platform, String action, String result, String actual, String expected) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        platformSelect().selected = platform
        environmentsSelect().selected = environment
        scrollToBottom()
        stepsTable.selectStepsTab('free-form')
        stepsTable.addStep(action, result)
        expectedInput = expected
        actualInput = actual
        createButton.click()
    }

    /**
     * fills in all information for the create form
     * but does not submit
     */
    void completeCreateForm() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        scrollToBottom()
        stepsTable.selectStepsTab('free-form')
        stepsTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
        actualInput = faker.beer().name()
        expectedInput = faker.beer().name()
    }
}
