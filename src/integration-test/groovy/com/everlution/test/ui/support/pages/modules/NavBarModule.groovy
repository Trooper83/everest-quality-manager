package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.error.RequiredPageContentNotPresent

class NavBarModule extends Module {

    static content = {
        loginLink { $("[data-test-id=main-login-link]") }
        logoutButton { $("[data-test-id=main-logout-button]")}
        usernameLabel { $("[data-test-id=main-welcome-username]")}
    }

    /**
     * Determines if the welcome message is displayed
     * @return - true if displayed, false if not
     */
    boolean isWelcomeMessageDisplayed() {
        try {
            usernameLabel.displayed
        } catch(RequiredPageContentNotPresent ignored) {
            return false
        }
        return true
    }

    /**
     * logs the user out of the app
     */
    void logout() {
        logoutButton.click()
    }

    /**
     * Verifies the welcome message text
     * @param username - the logged in user
     */
    boolean verifyWelcomeMessage(String username) {
        return usernameLabel.text() == "Welcome Back ${username}!"
    }
}
