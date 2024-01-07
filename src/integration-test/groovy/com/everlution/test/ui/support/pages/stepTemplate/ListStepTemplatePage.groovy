package com.everlution.test.ui.support.pages.stepTemplate

import com.everlution.test.ui.support.pages.common.ListPage
import geb.module.TextInput

class ListStepTemplatePage extends ListPage {

    static at = { title == "Step Template List" }
    static String convertToPath(Long projectId) {
        "/project/${projectId}/stepTemplates"
    }

    static content = {}
}
