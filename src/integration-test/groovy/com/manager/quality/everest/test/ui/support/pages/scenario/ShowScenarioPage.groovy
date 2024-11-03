package com.manager.quality.everest.test.ui.support.pages.scenario

import com.manager.quality.everest.test.ui.support.pages.common.ShowPage

class ShowScenarioPage extends ShowPage {
    static at = { title == "Scenario Details" }
    static convertToPath(Long projectId, Long id) {
        "/project/${projectId}/scenario/show/${id}"
    }

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
        return environmentsList.find("div > div > p")*.text().containsAll(names)
    }
}
