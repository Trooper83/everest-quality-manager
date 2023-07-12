package com.everlution.test.ui.support.pages.common

import com.everlution.test.ui.support.pages.modules.NavBarModule
import geb.Page

abstract class BasePage extends Page {

    static content = {
        navBar { module NavBarModule }
    }

    /**
     * scrolls the page to the bottom of the view, Geb has a hard time
     * scrolling and clicking
     */
    void scrollToBottom() {
        //Need to scroll button into view
        Thread.sleep(500)
        driver.executeScript("window.scrollBy(0,document.body.scrollHeight)")
        Thread.sleep(500)
    }
}
