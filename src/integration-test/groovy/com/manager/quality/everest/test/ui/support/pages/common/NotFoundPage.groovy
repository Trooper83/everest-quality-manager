package com.manager.quality.everest.test.ui.support.pages.common

class NotFoundPage extends BasePage {
    static at = { title == "Page Not Found"}

    static content = {
        errors { $("ul.errors>li") }
    }
}
