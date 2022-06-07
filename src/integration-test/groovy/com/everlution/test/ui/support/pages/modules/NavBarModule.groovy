package com.everlution.test.ui.support.pages.modules

import geb.Module

class NavBarModule extends Module {

    static content = {
        adminButton { $("#adminButton") }
        adminDropDownMenu { $("#adminDropDownMenu") }
        loginLink { $("[data-test-id=main-login-link]") }
        logoutButton { $("[data-test-id=main-logout-button]") }
    }

    /**
     * clicks and admin option
     */
    void goToAdminPage(String page) {
        adminButton.click()
        adminDropDownMenu.find('a', text: page).click()
    }

    /**
     * determines if an option is displayed
     */
    boolean isOptionDisplayed(String option) {
        adminButton.click()
        return adminDropDownMenu.find('a', text: option)
    }

    /**
     * logs the user out of the app
     */
    void logout() {
        logoutButton.click()
    }
}
