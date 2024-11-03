package com.manager.quality.everest.test.ui.support.pages.modules

import geb.Module
import geb.module.TextInput

class ListSearchModule extends Module {

    static content = {
        searchInput { $("#searchTerm").module(TextInput) }
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
     */
    void search(String term) {
        searchInput << term
        searchButton.click()
    }
}
