package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage

class EditTestCasePage extends BasePage {
    static url = "/testCase/edit"
    static at = { title == "Edit TestCase" }

    static content = {
        errorText { $("div.errors") }
    }
}
