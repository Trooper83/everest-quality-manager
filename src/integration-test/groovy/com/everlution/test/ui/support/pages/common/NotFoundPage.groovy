package com.everlution.test.ui.support.pages.common

import com.everlution.test.ui.support.pages.common.BasePage

class NotFoundPage extends BasePage {
    static at = { title == "Page Not Found"}

    static content = {
        errors { $("ul.errors>li") }
    }
}
