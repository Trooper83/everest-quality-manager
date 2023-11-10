package com.everlution.test.ui.support.pages.step

import com.everlution.test.ui.support.pages.common.ListPage
import geb.module.TextInput

class ListStepPage extends ListPage {

    static at = { title == "Step List" }
    static String convertToPath(Long projectId) {
        "/project/${projectId}/steps"
    }

    static content = {
        nameInput { $("#name").module(TextInput) }
        resetLink { $("#resetLink") }
        searchButton { $("#searchButton") }
    }

    /**
     * resets the search
     */
    void resetSearch() {
        resetLink.click()
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
