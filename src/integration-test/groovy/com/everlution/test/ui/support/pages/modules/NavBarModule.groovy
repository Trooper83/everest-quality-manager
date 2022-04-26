package com.everlution.test.ui.support.pages.modules

import geb.Module

class NavBarModule extends Module {

    static content = {
        createMenuDropDown(required: false) { $("#createMenu" ) }
        createMenuOptions { createMenuDropDown.find('a') }
        listsMenuDropDown(required: false) { $("#listsMenu" ) }
        loginLink { $("[data-test-id=main-login-link]") }
        logoutButton { $("[data-test-id=main-logout-button]") }
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

    /**
     * determines if a menu option is displayed
     */
    boolean isCreateMenuOptionDisplayed(String option) {
        return createMenuOptions*.text().contains(option)
    }

    /**
     * logs the user out of the app
     */
    void logout() {
        logoutButton.click()
    }
}
