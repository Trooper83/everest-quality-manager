package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.TableModule

class ListBugPage extends BasePage {
    static url = "/bug/index"
    static at = { title == "Bug List" }

    static content = {
        bugTable { module TableModule }
        createBugLink(required: false) { $("[data-test-id=index-create-link]") }
        homeLink { $("[data-test-id=index-home-link]") }
        statusMessage { $("div.message") }
    }

    /**
     * clicks the new bug button
     */
    void goToCreateBug() {
        createBugLink.click()
    }

    /**
     * clicks the home link
     */
    void goToHome() {
        homeLink.click()
    }
}
