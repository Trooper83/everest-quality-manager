package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.ListPage
import com.everlution.test.ui.support.pages.modules.SideBarModule
import geb.module.TextInput

class ListTestCasePage extends ListPage {
    static at = { title == "TestCase List" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/testCases"
    }
}
