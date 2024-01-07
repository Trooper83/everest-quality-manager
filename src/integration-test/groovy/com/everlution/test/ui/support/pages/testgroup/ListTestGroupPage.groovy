package com.everlution.test.ui.support.pages.testgroup

import com.everlution.test.ui.support.pages.common.ListPage
import geb.module.TextInput

class ListTestGroupPage extends ListPage {

    static at = { title == "TestGroup List" }

    String convertToPath(Long projectId) {
        "project/${projectId}/testGroups"
    }
}
