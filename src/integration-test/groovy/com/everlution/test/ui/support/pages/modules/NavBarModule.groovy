package com.everlution.test.ui.support.pages.modules

import geb.Module

class NavBarModule extends Module {

    static content = {
        loginLink { $("[data-test-id=main-login-link]") }
        logoutButton { $("[data-test-id=main-logout-button]") }
    }

    /**
     * logs the user out of the app
     */
    void logout() {
        logoutButton.click()
    }
}
