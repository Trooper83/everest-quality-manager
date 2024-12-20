package com.manager.quality.everest.test.ui.support.pages.bug


import com.github.javafaker.Faker
import com.manager.quality.everest.test.ui.support.pages.common.CreatePage
import com.manager.quality.everest.test.ui.support.pages.modules.StepTableModule
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
        notesInput { $("#notes") }
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
        stepsTable.appendBuilderSteps()
        expectedInput = expected
        actualInput = actual
        scrollToBottom()
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
        stepsTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(5),
                faker.lorem().sentence(7))
        createButton.click()
    }

    /**
     * creates a bug with the supplied data
     */
    void createFreeFormBug(String name, String description, String area, List<String> environment,
                           String platform, String notes, String action, String data, String result, String actual, String expected) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        platformSelect().selected = platform
        environmentsSelect().selected = environment
        notesInput = notes
        scrollToBottom()
        stepsTable.addStep(action, data, result)
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
        stepsTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(5),
                faker.lorem().sentence(7))
        actualInput = faker.beer().name()
        expectedInput = faker.beer().name()
    }
}
