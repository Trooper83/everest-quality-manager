package com.everlution.test.ui.support.pages.automatedtest

import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.TableModule

class ShowAutomatedTestPage extends ShowPage {
    static at = { title == "Automated Test Details" }

    static String convertToPath(Long projectId, Long testId) {
        "/project/${projectId}/automatedTest/show/${testId}"
    }

    static content = {
        allTimePassValue { $("#allTimePassPercent") }
        recentPassValue { $("#recentPassPercent") }
        resultsTable { module TableModule }
    }
}
