package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.StepTableModule
import com.github.javafaker.Faker
import geb.module.MultipleSelect
import geb.module.Select

class CreateBugPage extends BasePage {
    static url = "/bug/create"
    static at = { title == "Create Bug" }

    static content = {
        actualInput { $("#actual") }
        areaOptions { $("#area>option") }
        createButton { $("#create") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        errorsMessage { $("ul.errors") }
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
     * determines if the required field indication (asterisk) is
     * displayed for the supplied fields
     * @param fields - list of fields
     * @return - true if all fields have the indicator, false if at least one does not
     */
    boolean areRequiredFieldIndicatorsDisplayed(List<String> fields) {
        for(field in fields) {
            def sel = $("label[for=${field}]>span.required-indicator")
            if (!sel.displayed) {
                return false
            }
        }
        return true
    }

    /**
     * adds a generic bug
     */
    void createBug() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        actualInput = faker.beer().name()
        expectedInput = faker.beer().name()
        stepsTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
        createButton.click()
    }

    /**
     * creates a bug with the supplied data
     */
    void createBug(String name, String description, String area, List<String> environment,
                   String platform, String action, String result, String actual, String expected) {
        nameInput = name
        descriptionInput = description
        areaSelect().selected = area
        platformSelect().selected = platform
        environmentsSelect().selected = environment
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
        stepsTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
        actualInput = faker.beer().name()
        expectedInput = faker.beer().name()
    }

    /**
     * Gets the labels for all fields displayed on the page
     * @return - a list of field names
     */
    List<String> getFields() {
        return fieldLabels*.text()
    }

    /**
     * clicks the home link
     */
    void goToHome() {
        homeLink.click()
    }

    /**
     * clicks the list link
     */
    void goToList() {
        listLink.click()
    }
}
