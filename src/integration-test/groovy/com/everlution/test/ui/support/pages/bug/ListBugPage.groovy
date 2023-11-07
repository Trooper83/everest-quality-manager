package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.ListPage
import com.everlution.test.ui.support.pages.modules.TableModule
import geb.module.TextInput

class ListBugPage extends ListPage {

    static at = { title == "Bug List" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/bugs"
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
