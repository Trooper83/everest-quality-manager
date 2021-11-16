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
        areaOptions { $("#area>option") }
        createButton { $("#create") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        errorsMessage { $("ul.errors") }
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link]") }
        nameInput { $("#name") }
        projectOptions { $("#project>option") }
        stepsTable { module StepTableModule }
    }

    /**
     * select elements strongly typed for convenience in tests
     */
    Select areaSelect() {
        $("#area").module(Select)
    }

    /**
     * select elements strongly typed for convenience in tests
     */
    MultipleSelect environmentsSelect() {
        $("#environments").module(MultipleSelect)
    }

    /**
     * select element strongly typed for convenience in tests
     */
    Select projectSelect() {
        $("#project").module(Select)
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
        projectSelect().selected = "1"
        stepsTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
        createButton.click()
    }

    /**
     * creates a bug with the supplied data
     */
    void createBug(String name, String description, String area, List<String> environment,
                   String project, String action, String result) {
        nameInput = name
        descriptionInput = description
        projectSelect().selected = project
        waitFor() {
            areaSelect().enabled
        }
        areaSelect().selected = area
        environmentsSelect().selected = environment
        stepsTable.addStep(action, result)
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
        projectSelect().selected = "1"
        stepsTable.addStep(faker.lorem().sentence(5), faker.lorem().sentence(7))
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
