package com.everlution.test.ui.support.pages.common

class DeniedPage extends BasePage {
    static at = { title == "Denied" }

    static content = {
        errors { $("div.errors") }
    }
}
