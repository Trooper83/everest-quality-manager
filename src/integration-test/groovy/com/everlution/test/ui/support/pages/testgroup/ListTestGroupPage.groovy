package com.everlution.test.ui.support.pages.testgroup

import com.everlution.test.ui.support.pages.common.ListPage

class ListTestGroupPage extends ListPage {
    static url = "/testGroup/index"
    static at = { title == "TestGroup List" }

}
