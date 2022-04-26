package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage

class ProjectHomePage extends BasePage {
    static url = "/home"
    static at = { title == "Project Home" }
}
