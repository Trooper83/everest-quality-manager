package com.manager.quality.everest.test.ui.support.pages.stepTemplate

import com.manager.quality.everest.test.ui.support.pages.common.ListPage

class ListStepTemplatePage extends ListPage {

    static at = { title == "Step Template List" }
    static String convertToPath(Long projectId) {
        "/project/${projectId}/stepTemplates"
    }

    static content = {}
}
