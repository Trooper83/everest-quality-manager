package com.everlution.test.ui.support.pages.releaseplan

import com.everlution.test.ui.support.pages.common.BasePage
import com.github.javafaker.Faker
import geb.module.Select

class CreateReleasePlanPage extends BasePage {
    static url = "/releasePlan/create"
    static at = { title == "Create ReleasePlan" }

    static content = {
        createButton { $("#create") }
        errorsMessage { $("ul.errors") }
        fieldLabels { $("fieldset label") }
        homeLink { $("[data-test-id=create-home-link]") }
        listLink { $("[data-test-id=create-list-link]") }
        nameInput { $("#name") }
        projectOptions { $("#project>option") }
    }

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
     * creates a generic plan
     */
    void createReleasePlan() {
        Faker faker = new Faker()
        projectSelect().selected = "1"
        nameInput << faker.name().title()
        createButton.click()
    }

    /**
     * creates a plan with the supplied data
     */
    void createReleasePlan(String name, String project) {
        projectSelect().selected = project
        nameInput << name
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
