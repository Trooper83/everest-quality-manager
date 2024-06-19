package com.everlution.test.ui.support.pages.scenario

import com.everlution.test.ui.support.pages.common.CreatePage
import com.github.javafaker.Faker
import geb.module.MultipleSelect
import geb.module.Select
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys
import org.openqa.selenium.support.ui.ExpectedConditions

class CreateScenarioPage extends CreatePage {
    static at = { title == "Create Scenario" }
    static convertToPath(Long projectId) {
        "/project/${projectId}/scenario/create"
    }

    static content = {
        areaOptions { $("#area>option") }
        createButton { $("#create") }
        descriptionInput { $("#description") }
        environmentsOptions { $("#environments>option") }
        errorsMessage { $("ul.errors") }
        executionMethodOptions { $("#executionMethod>option") }
        fieldLabels { $("fieldset label") }
        gherkinTextArea { $("#gherkin") }
        nameInput { $("#name") }
        platformOptions { $("#platform>option") }
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

    Select typeSelect() {
        $("#type").module(Select)
    }

    /**
     * adds a generic scenario
     */
    void createScenario() {
        Faker faker = new Faker()
        nameInput = faker.zelda().game()
        descriptionInput = faker.zelda().character()
        gherkinTextArea = faker.lorem().sentence(5)
        //Need to scroll button into view
        Thread.sleep(500)
        driver.executeScript("window.scrollBy(0,document.body.scrollHeight)")
        Thread.sleep(500)
        createButton.click()
    }

    /**
     * creates a scenario with the supplied data
     */
    void createScenario(String name, String description, String gherkin, String area, List<String> environments,
                        String method, String type, String platform) {
        nameInput = name
        descriptionInput = description
        executionMethodSelect().selected = method
        typeSelect().selected = type
        areaSelect().selected = area
        platformSelect().selected = platform
        gherkinTextArea = gherkin
        environmentsSelect().selected = environments
        //Need to scroll button into view
        Thread.sleep(500)
        driver.executeScript("window.scrollBy(0,document.body.scrollHeight)")
        Thread.sleep(500)
        createButton.click()
    }
}
