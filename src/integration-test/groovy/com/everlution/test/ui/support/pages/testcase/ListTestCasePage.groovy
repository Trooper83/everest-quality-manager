package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.ListPage
import geb.module.TextInput

class ListTestCasePage extends ListPage {
    static at = { title == "TestCase List" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/testCases"
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
