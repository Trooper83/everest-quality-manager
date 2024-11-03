package com.manager.quality.everest.test.ui.support.pages.project

import com.manager.quality.everest.test.ui.support.pages.common.ListPage
import com.manager.quality.everest.test.ui.support.pages.modules.TableModule

class ListProjectPage extends ListPage {
    static url = "/projects"
    static at = { title == "Projects List" }

    static content = {
        projectTable { module TableModule }
    }
}
