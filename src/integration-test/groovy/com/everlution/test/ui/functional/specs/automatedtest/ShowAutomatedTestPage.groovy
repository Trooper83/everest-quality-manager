package com.everlution.test.ui.functional.specs.automatedtest

import com.everlution.test.ui.support.pages.common.ShowPage

class ShowAutomatedTestPage extends ShowPage {
    static at = { title == "Automated Test Details" }

    static String convertToPath(Long projectId, Long testId) {
        "/project/${projectId}/automatedTest/show/${testId}"
    }

    static content = {}
}
