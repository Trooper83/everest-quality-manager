package com.everlution.test.ui.support.pages

class EditTestCasePage extends BasePage {
    static url = "/testCase/edit"
    static at = { title == "Edit TestCase" }

    static content = {
        errorText { $("div.errors") }
    }
}
