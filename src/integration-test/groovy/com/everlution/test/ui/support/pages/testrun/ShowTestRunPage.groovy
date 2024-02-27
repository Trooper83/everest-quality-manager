package com.everlution.test.ui.support.pages.testrun

import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.TableModule

class ShowTestRunPage extends ShowPage {
    static at = { title == "Test Run Details" }

    static String convertToPath(Long projectId, Long runId) {
        "/project/${projectId}/testRun/show/${runId}"
    }

    static content = {
        resultsTable { module TableModule }
    }
}
