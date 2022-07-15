package com.everlution.test.ui.support.pages.modules

import geb.Module

class ProjectNavModule extends Module {

    static content = {
        createMenuButton(required: false) { $("#dropdownMenuButton" ) }
        createMenuDropDown(required: false) { $("#createMenu" ) }
    }

    /**
     * determines if the create menu is open
     */
    boolean isCreateMenuOpen() {
        return createMenuButton.attr("aria-expanded") == "true"
    }

    /**
     * goes to an items create page
     * @param item
     */
    void goToCreatePage(String item) {
        createMenuButton.click()
        createMenuDropDown.find('a', text: item).click()
    }

    /**
     * displays the create menu
     */
    void openCreateMenu() {
        createMenuButton.click()
    }
}
