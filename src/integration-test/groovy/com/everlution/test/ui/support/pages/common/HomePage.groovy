package com.everlution.test.ui.support.pages.common

import com.everlution.test.ui.support.pages.common.BasePage

class HomePage extends BasePage {
    static url = "/"
    static at = { title == "Welcome to Grails" }
}
