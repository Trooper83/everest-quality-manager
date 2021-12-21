package com.everlution.test.ui.support.pages.releaseplan

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.TableModule

class ListReleasePlanPage extends BasePage {
    static url = "/releasePlan/index"
    static at = { title == "ReleasePlan List" }

    static content = {
        createLink(required: false) { $("[data-test-id=index-create-link]") }
        homeLink { $("[data-test-id=index-home-link]") }
        statusMessage { $("div.message") }
        plansTable { module TableModule }
    }

    /**
     * clicks the new project button
     */
    void goToCreatePlan() {
        createLink.click()
    }

    /**
     * clicks the home link
     */
    void goToHome() {
        homeLink.click()
    }
}
