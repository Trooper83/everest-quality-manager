package com.everlution.test.ui.support.pages.scenario

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.TableModule

class ListScenarioPage extends BasePage {
    static url = "/scenario/index"
    static at = { title == "Scenario List" }

    static content = {
        createScenarioLink(required: false) { $("[data-test-id=index-create-button]") }
        homeLink { $("[data-test-id=index-home-link]") }
        statusMessage { $("div.message") }
        scenarioTable { module TableModule }
    }

    /**
     * clicks the new scenario button
     */
    void goToCreateScenario() {
        createScenarioLink.click()
    }

    /**
     * clicks the home link
     */
    void goToHome() {
        homeLink.click()
    }
}
