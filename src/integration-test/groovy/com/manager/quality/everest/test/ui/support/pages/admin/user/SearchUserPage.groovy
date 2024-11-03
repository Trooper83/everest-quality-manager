package com.manager.quality.everest.test.ui.support.pages.admin.user


import com.manager.quality.everest.test.ui.support.pages.common.BasePage
import com.manager.quality.everest.test.ui.support.pages.modules.TableModule

class SearchUserPage extends BasePage {
    static url = "/user/search"
    static at = { title == "User Search" }

    private statusMap = ['true': '1', 'false': '-1', 'either': '0']

    static content = {
        emailInput { $("#email") }
        resultsTable { module TableModule }
        searchButton { $("#searchButton") }
    }

    /**
     * searches for a user by text and status [true, false or either]
     */
    void search(String text, LinkedHashMap<String, String> statuses) {
        emailInput << text
        statuses.each { key, value ->
            def v = statusMap.get(value)
            $("[name=${key}][value='${v}']").click()
        }
        searchButton.click()
    }
}
