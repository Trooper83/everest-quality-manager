package com.everlution.test.ui.support.pages.testgroup

import com.everlution.test.ui.support.pages.common.ShowPage

class ShowTestGroupPage extends ShowPage {
    static url = "/testGroup/show"
    static at = { title == "Show TestGroup" }

    static content = {
        nameValue { $("#name") }
        projectValue { $("#project") }
    }
}
