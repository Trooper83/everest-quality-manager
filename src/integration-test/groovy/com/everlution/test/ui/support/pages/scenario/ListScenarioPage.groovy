package com.everlution.test.ui.support.pages.scenario

import com.everlution.test.ui.support.pages.common.ListPage
import com.everlution.test.ui.support.pages.modules.TableModule
import geb.module.TextInput

class ListScenarioPage extends ListPage {

    static at = { title == "Scenario List" }
    static convertToPath(Long projectId) {
        "/project/${projectId}/scenarios"
    }

    static content = {
        nameInput { $("#name").module(TextInput) }
        searchButton { $("#searchButton") }
    }

    /**
     * performs a search
     * @param name - name to search for
     */
    void search(String name) {
        nameInput << name
        searchButton.click()
    }
}
