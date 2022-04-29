package com.everlution.test.ui.support.pages.modules

import geb.Module

class ProjectNavModule extends Module {

    static content = {
        createMenuDropDown(required: false) { $("#createMenu" ) }
        createMenuOptions { createMenuDropDown.find('a') }
        listsMenuDropDown(required: false) { $("#listsMenu" ) }
    }

    /**
     * goes to an items create page
     * @param item
     */
    void goToCreatePage(String item) {
        createMenuDropDown.click()
        createMenuDropDown.find('a', text: item).click()
    }

    /**
     * goes to an items list page
     * @param item
     */
    void goToListsPage(String item) {
        listsMenuDropDown.click()
        listsMenuDropDown.find('a', text: item).click()
    }
}
