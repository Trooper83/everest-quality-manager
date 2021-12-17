package com.everlution.test.ui.support.pages.scenario

import com.everlution.test.ui.support.pages.common.BasePage
import com.github.javafaker.Faker
import geb.module.MultipleSelect
import geb.module.Select

class CreateScenarioPage extends BasePage {
    static url = "/scenario/create"
    static at = { title == "Create Scenario" }

    static content = {
        areaOptions { $("#area>option") }
        createButton { $("#create") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        errorsMessage { $("ul.errors") }
        executionMethodOptions { $("#executionMethod>option") }
        fieldLabels { $("fieldset label") }
        gherkinTextArea { $("#gherkin") }
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link]") }
        nameInput { $("#name") }
        platformOptions { $("#platform>option") }
        projectOptions { $("#project>option") }
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

    Select projectSelect() {
        $("#project").module(Select)
    }

    Select typeSelect() {
        $("#type").module(Select)
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
     * adds a generic scenario
     */
    void createScenario() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        projectSelect().selected = "1"
        gherkinTextArea = faker.lorem().sentence(5)
        createButton.click()
    }

    /**
     * creates a scenario with the supplied data
     */
    void createScenario(String name, String description, String gherkin, String project, String area, List<String> environments,
                        String method, String type, String platform) {
        nameInput = name
        descriptionInput = description
        projectSelect().selected = project
        executionMethodSelect().selected = method
        typeSelect().selected = type
        areaSelect().selected = area
        environmentsSelect().selected = environments
        platformSelect().selected = platform
        gherkinTextArea = gherkin
        createButton.click()
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
