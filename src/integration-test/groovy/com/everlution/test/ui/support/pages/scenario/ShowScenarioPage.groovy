package com.everlution.test.ui.support.pages.scenario

import com.everlution.test.ui.support.pages.common.ShowPage

class ShowScenarioPage extends ShowPage {
    static url = "/scenario/show"
    static at = { title == "Show Scenario" }

    static content = {
        areaValue { $("#area") }
        creatorValue { $("#creator") }
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        descriptionValue { $("#description") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        environmentsList { $("#environments") }
        executionMethodValue { $("#executionMethod") }
        gherkinTextArea { $("#gherkin") }
        nameValue { $("#name") }
        platformValue { $("#platform") }
        projectValue { $("#project") }
        statusMessage { $("div.message") }
        typeValue { $ ("#type") }
    }

    /**
     * clicks the delete link
     */
    void deleteScenario() {
        withConfirm(true) { deleteLink.click() }
    }

    /**
     * determines if an environment is displayed
     * @param names - name of the environment to check
     */
    boolean areEnvironmentsDisplayed(List<String> names) {
        return environmentsList.find("div")*.text().containsAll(names)
    }
}
