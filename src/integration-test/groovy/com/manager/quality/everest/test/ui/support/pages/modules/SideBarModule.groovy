package com.manager.quality.everest.test.ui.support.pages.modules

import geb.Module

class SideBarModule extends Module {

    static content = {
        sidebar { $("#sidebarMenu") }
        createButton(required: false) { sidebar.find('#dropdownCreateButton') }
        createMenu { $('#dropdownCreateMenu') }
    }

    /**
     * goes to create {domain} page
     */
    void goToCreate(String domain) {
        createButton.click()
        createMenu.find('a', text: domain).click()
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
