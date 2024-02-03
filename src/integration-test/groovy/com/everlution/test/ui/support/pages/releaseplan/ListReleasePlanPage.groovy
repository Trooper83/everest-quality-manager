package com.everlution.test.ui.support.pages.releaseplan

import com.everlution.test.ui.support.pages.common.ListPage
import geb.module.TextInput

class ListReleasePlanPage extends ListPage {
    static at = { title == "ReleasePlan List" }

    static String convertToPath(Long id) {
        "/project/${id}/releasePlans"
    }
}
