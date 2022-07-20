package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.ListPage
import com.everlution.test.ui.support.pages.modules.TableModule
import geb.module.TextInput

class ListProjectPage extends ListPage {
    static url = "/projects"
    static at = { title == "Project List" }

    static content = {
        nameInput { $("#name").module(TextInput) }
        searchButton { $("#searchButton") }
        projectTable { module TableModule }
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
