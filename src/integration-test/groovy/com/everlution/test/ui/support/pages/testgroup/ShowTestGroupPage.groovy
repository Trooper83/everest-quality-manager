package com.everlution.test.ui.support.pages.testgroup

import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.TableModule

class ShowTestGroupPage extends ShowPage {
    static at = { title == "Show TestGroup" }

    String convertToPath(Long projectId, Long groupId) {
        "/project/${projectId}/testGroup/show/${groupId}"
    }

    static content = {
        nameValue { $("#name") }
        projectValue { $("#project") }
        testCaseTable { module TableModule }
    }
}