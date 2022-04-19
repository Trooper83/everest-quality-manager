package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.TableModule

class ListProjectPage extends BasePage {
    static url = "/projects"
    static at = { title == "Project List" }

    static content = {
        createProjectLink(required: false) { $("[data-test-id=index-create-link]") }
        homeLink { $("[data-test-id=index-home-link]") }
        statusMessage { $("div.message") }
        projectTable { module TableModule }
    }

    /**
     * clicks the new project button
     */
    void goToCreateProject() {
        createProjectLink.click()
    }

    /**
     * clicks the home link
     */
    void goToHome() {
        homeLink.click()
    }
}
