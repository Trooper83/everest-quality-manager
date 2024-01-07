package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.module.TextInput

class ListSearchModule extends Module {

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
