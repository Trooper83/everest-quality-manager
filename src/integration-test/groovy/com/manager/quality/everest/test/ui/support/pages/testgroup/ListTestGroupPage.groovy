package com.manager.quality.everest.test.ui.support.pages.testgroup

import com.manager.quality.everest.test.ui.support.pages.common.ListPage

class ListTestGroupPage extends ListPage {

    static at = { title == "TestGroup List" }

    String convertToPath(Long projectId) {
        "project/${projectId}/testGroups"
    }
}
