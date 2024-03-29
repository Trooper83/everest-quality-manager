package com.everlution.test.ui.support.pages.automatedtest

import com.everlution.test.ui.support.pages.common.ListPage

class ListAutomatedTestPage extends ListPage {
    static at = { title == "Automated Test List" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/automatedTests"
    }
}