package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.ListPage
import com.everlution.test.ui.support.pages.modules.TableModule
import geb.module.TextInput

class ListProjectPage extends ListPage {
    static url = "/projects"
    static at = { title == "Projects List" }

    static content = {
        projectTable { module TableModule }
    }
}
