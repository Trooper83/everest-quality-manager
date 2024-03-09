package com.everlution.test.ui.support.pages.automatedtest

import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.TableModule

class ShowAutomatedTestPage extends ShowPage {
    static at = { title == "Automated Test Details" }

    static String convertToPath(Long projectId, Long testId) {
        "/project/${projectId}/automatedTest/show/${testId}"
    }

    static content = {
        allTimeFailValue { $("#allTimeFail") }
        allTimePassValue { $("#allTimePass") }
        allTimePassPercentValue { $("#allTimePassPercent") }
        allTimeSkipValue { $("#allTimeSkip") }
        allTimeTotalValue { $("#allTimeTotal") }
        recentFailValue { $("#recentFail") }
        recentPassValue { $("#recentPass") }
        recentSkipValue { $("#recentSkip") }
        recentTotalValue { $("#recentTotal") }
        recentPassPercentValue { $("#recentPassPercent") }
        resultsTable { module TableModule }
    }
}
