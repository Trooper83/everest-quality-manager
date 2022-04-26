package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.ListPage
import com.everlution.test.ui.support.pages.modules.TableModule

class ListBugPage extends ListPage {
    static url = "/bugs"
    static at = { title == "Bug List" }

    static content = {
        bugTable { module TableModule }
        statusMessage { $("div.message") }
    }
}
