package com.everlution.test.ui.support.pages.modules

import geb.Module

class SideBarModule extends Module {

    static content = {
        sidebar { $("#sidebarMenu") }
    }

    /**
     * goes to a project domain
     */
    void goToProjectDomain(String domain) {
        sidebar.find('a', text: domain).click()
    }

    /**
     * goes to a project's home
     */
    void goToProjectHome() {
        sidebar.find('a').first().click()
    }
}
