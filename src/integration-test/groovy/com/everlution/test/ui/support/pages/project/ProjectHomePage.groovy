package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.SideBarModule

class ProjectHomePage extends BasePage {
    static url = "/home"
    static at = { title == "Project Home" }

    static content = {
        adminButton(required: false) { $("#adminButton") }
        errorsMessage { $("ul.errors") }
        nextReleaseLink(required: false) { $("#releasePlanCard #nextLink") }
        previousReleaseLink(required: false) { $("#releasePlanCard #previousLink") }
        sideBar { module SideBarModule }
        statusMessage { $("div.message") }
    }
}
