package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.ListPage
import com.everlution.test.ui.support.pages.modules.TableModule
import geb.module.TextInput

class ListProjectPage extends ListPage {
    static url = "/projects"
    static at = { title == "Project List" }

    static content = {
        createProjectLink(required: false) { $("[data-test-id=index-create-link]") }
        homeLink { $("[data-test-id=index-home-link]") }
        nameInput { $("#name").module(TextInput) }
        searchButton { $("#searchButton") }
        statusMessage { $("div.message") }
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
