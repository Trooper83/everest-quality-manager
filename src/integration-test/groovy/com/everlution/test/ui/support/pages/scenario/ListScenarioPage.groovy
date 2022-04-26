package com.everlution.test.ui.support.pages.scenario

import com.everlution.test.ui.support.pages.common.ListPage
import com.everlution.test.ui.support.pages.modules.TableModule

class ListScenarioPage extends ListPage {
    static url = "/scenarios"
    static at = { title == "Scenario List" }

    static content = {
        createScenarioLink(required: false) { $("[data-test-id=index-create-button]") }
        statusMessage { $("div.message") }
        scenarioTable { module TableModule }
    }

    /**
     * clicks the new scenario button
     */
    void goToCreateScenario() {
        createScenarioLink.click()
    }
}
