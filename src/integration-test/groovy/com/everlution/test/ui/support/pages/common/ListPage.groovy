package com.everlution.test.ui.support.pages.common
import com.everlution.test.ui.support.pages.modules.TableModule

class ListPage extends BasePage {
    static content = {
        statusMessage { $("div.alert") }
        listTable { module TableModule }
    }
}
