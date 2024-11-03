package com.manager.quality.everest.test.ui.support.pages.testrun


import com.manager.quality.everest.test.ui.support.pages.common.ShowPage
import com.manager.quality.everest.test.ui.support.pages.modules.TableModule

class ShowTestRunPage extends ShowPage {
    static at = { title == "Test Run Details" }

    static String convertToPath(Long projectId, Long runId) {
        "/project/${projectId}/testRun/show/${runId}"
    }

    static content = {
        failedCauseElement(required: false) { $("[data-test-id=failureCause]") }
        failValue { $("#fail") }
        passValue { $("#pass") }
        passPercentValue { $("#passPercent") }
        skipValue { $("#skip") }
        totalValue { $("#total") }
        resultsTable { module TableModule }
    }

    void expandFailureCause() {
        def cells = $("tbody tr")[0].find("td")
        cells[0].find("button").click()
        waitFor {
            $('[data-test-id=failureCause]').isDisplayed()
        }
    }
}
