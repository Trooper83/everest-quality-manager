package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.module.Select

class LinkModule extends Module {

    static content = {
        addButton { $('#btnAdd') }
        links { $('#linkedSteps .card') }
        linkedItems(required: false) { $("#linkedSteps .card") }
        searchInput { $('#search') }
        searchResults { $('.search-results-menu-item') }
        searchResultsMenu { $('#search-results') }
        tooltip(wait: true) { $("div[role=tooltip]") }
        validationMessage { $('div.text-danger') }
    }

    Select relationSelect() {
        $("#relation").module(Select)
    }

    /**
     * adds a link using the supplied data
     */
    void addLink(String text, String relation) {
        relationSelect().selected = relation
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            searchInput << s
        }
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
    boolean isLinkDisplayed(String text, String relation) {
        for (def item : linkedItems) {
            if (item.find('[data-test-id=linkedItemName]').text().endsWith(text) &&
                    item.find('[data-test-id=linkedItemRelation]').text().endsWith(relation)) {
                return true
            }
        }
        return false
    }

    /**
     * gets the text of the displayed tooltip
     */
    String getToolTipText() {
        waitFor() {
            tooltip.displayed
        }
        return tooltip.text()
    }
}
