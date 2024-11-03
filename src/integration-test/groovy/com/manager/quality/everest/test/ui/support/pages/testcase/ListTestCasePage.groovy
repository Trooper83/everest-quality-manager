package com.manager.quality.everest.test.ui.support.pages.testcase

import com.manager.quality.everest.test.ui.support.pages.common.ListPage

class ListTestCasePage extends ListPage {
    static at = { title == "TestCase List" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/testCases"
    }
}
