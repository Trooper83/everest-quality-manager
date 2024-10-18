package com.manager.quality.everest.test.ui.support.pages.bug


import com.manager.quality.everest.test.ui.support.pages.common.ListPage

class ListBugPage extends ListPage {

    static at = { title == "Bug List" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/bugs"
    }

    static content = {
    }
}
