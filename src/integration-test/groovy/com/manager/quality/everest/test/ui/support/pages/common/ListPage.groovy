package com.manager.quality.everest.test.ui.support.pages.common


import com.manager.quality.everest.test.ui.support.pages.modules.ListSearchModule
import com.manager.quality.everest.test.ui.support.pages.modules.SideBarModule
import com.manager.quality.everest.test.ui.support.pages.modules.TableModule

class ListPage extends BasePage {
    static content = {
        statusMessage { $("div.alert") }
        listTable { module TableModule }
        searchModule { module ListSearchModule }
        sideBar { module SideBarModule}
    }
}
