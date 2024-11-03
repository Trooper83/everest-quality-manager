package com.manager.quality.everest.test.ui.support.pages.scenario

import com.manager.quality.everest.test.ui.support.pages.common.ListPage

class ListScenarioPage extends ListPage {

    static at = { title == "Scenario List" }
    static convertToPath(Long projectId) {
        "/project/${projectId}/scenarios"
    }
}
