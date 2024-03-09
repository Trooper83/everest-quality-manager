package com.everlution.test.ui.support.pages.testrun

import com.everlution.test.ui.support.pages.common.ListPage

class ListTestRunPage extends ListPage {
    static at = { title == "Test Run List" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/testRuns"
    }
}
