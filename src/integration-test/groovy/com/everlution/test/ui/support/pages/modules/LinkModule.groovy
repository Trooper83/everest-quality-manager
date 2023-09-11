package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.module.Select
import geb.module.TextInput
import geb.navigator.Navigator

class LinkModule extends Module {

    static content = {
        addButton { $('#btnAdd') }
        linkedItems(required: false) { $("#links .card") }
        relationOptions { $('#relation>option') }
        searchInput { $('#search').module(TextInput) }
        searchResults { $('.search-results-menu-item') }
        searchResultsMenu { $('#search-results') }
        tooltip(wait: true) { $("div[role=tooltip]") }
        validationMessage(required: false) { $('div.text-danger') }
    }

    Select relationSelect() {
        $("#relation").module(Select)
    }

    void setLinkProperties(String text, String relation) {
        relationSelect().selected = relation
        searchInput.text = ''
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
    }

    /**
     * adds a link using the supplied data
     */
    void addLink(String text, String relation) {
        setLinkProperties(text, relation)
        addButton.click()
    }

    /**
     * determines if a linked item is displayed
     */
    boolean isLinkDisplayed(String text) {
        for (def item : linkedItems) {
            if (item.find('[data-test-id=linkedItemName]').text().endsWith(text)) {
                return true
            }
        }
        return false
    }

    /**
     * gets a linked item's hidden inputs
     */
    Navigator getLinkedItemHiddenInput(int index, String type) {
        def selector = type === 'id' ? "[id='links[${index}].linkedId']" : "[id='links[${index}].relation']"
        return linkedItems[index].find(selector)
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

    /**
     * removes a linked item
     */
    void removeLinkedItem(int index) {
        linkedItems[index].find('svg').click()
    }
}
