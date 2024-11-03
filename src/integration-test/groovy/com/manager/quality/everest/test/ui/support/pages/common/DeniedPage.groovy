package com.manager.quality.everest.test.ui.support.pages.common

class DeniedPage extends BasePage {
    static at = { title == "Denied" }

    static content = {
        errors { $("div.errors") }
    }
}
