package com.everlution.test.ui.support.pages.common

import com.everlution.test.ui.support.pages.modules.NavBarModule
import geb.Page

abstract class BasePage extends Page {

    static content = {
        navBar { module NavBarModule }
    }
}
