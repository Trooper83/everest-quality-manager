package com.everlution.test.ui.support.pages.common

import com.everlution.test.ui.support.pages.modules.TableModule

class ListPage extends BasePage {
    static content = {
        createLink(required: false) { $("[data-test-id=index-create-link]") }
        homeLink { $("[data-test-id=index-home-link]") }
        statusMessage { $("div.message") }
        listTable { module TableModule }
    }

    /**
     * clicks the create button
     */
    void goToCreate() {
        createLink.click()
    }

    /**
     * clicks the home link
     */
    void goToHome() {
        homeLink.click()
    }
}
