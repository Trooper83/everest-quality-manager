package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.module.Select

class LinkModule extends Module {

    static content = {
        addButton { $('#btnAdd') }
        links { $('#linkedSteps .card') }
        linkedItem(required: false) { text -> $("#linkedSteps .card p", text: text) }
        searchInput { $('#search') }
        searchResults { $('.search-results-menu-item') }
        searchResultsMenu { $('#search-results') }
    }

    Select relationSelect() {
        $("#relation").module(Select)
    }

    /**
     * adds a link using the supplied data
     */
    void addLink(String text, String relation) {
        relationSelect().selected = relation
        searchInput << text
        waitFor {
            searchResultsMenu.displayed
        }
        searchResults.first().click()
        searchResults.first().click()
        addButton.click()
    }

    /**
     * determines if a linked item is displayed
     */
    boolean isLinkDisplayed(text) {
        linkedItem(text).size() == 1
    }
}
