package com.everlution.test.ui.support.pages.modules

import geb.Module

class NavBarModule extends Module {

    static content = {
        createMenuDropDown { $("#createMenu" ) }
        listsMenuDropDown { $("#listsMenu" ) }
        loginLink { $("[data-test-id=main-login-link]") }
        logoutButton { $("[data-test-id=main-logout-button]") }
        usernameLabel { $("[data-test-id=main-welcome-username]") }
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
     * logs the user out of the app
     */
    void logout() {
        logoutButton.click()
    }
}
