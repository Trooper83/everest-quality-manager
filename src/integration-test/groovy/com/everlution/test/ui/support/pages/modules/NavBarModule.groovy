package com.everlution.test.ui.support.pages.modules

import geb.Module

class NavBarModule extends Module {

    static content = {
        adminButton(required: false) { $("#adminButton") }
        adminDropDownMenu { $("#adminDropDownMenu") }
        loginLink { $("[data-test-id=main-login-link]") }
        logoutButton { $("[data-test-id=main-logout-button]") }
        profileLink { $("#profileLink") }
    }

    /**
     * clicks and admin option
     */
    void goToAdminPage(String page) {
        adminButton.click()
        adminDropDownMenu.find('a', text: page).click()
    }

    /**
     * clicks the profile link
     */
    void goToProfile() {
        profileLink.click()
    }

    /**
     * determines if an option is displayed
     */
    boolean isOptionDisplayed(String option) {
        if(!adminDropDownMenu.displayed) { adminButton.click() }
        def count = adminDropDownMenu.find('a', text: option).size()
        return count == 1
    }

    /**
     * logs the user out of the app
     */
    void logout() {
        logoutButton.click()
    }
}
