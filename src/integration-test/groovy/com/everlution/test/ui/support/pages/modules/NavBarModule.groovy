package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.error.RequiredPageContentNotPresent

class NavBarModule extends Module {

    static content = {
        loginLink { $("[data-test-id=auth-login-link]") }
        logoutButton { $("[data-test-id=main-logout-button]")}
        usernameLabel { $("[data-test-id=main-welcome-username]")}
    }

    boolean isWelcomeMessageDisplayed() {
        try {
            usernameLabel.displayed
        } catch(RequiredPageContentNotPresent e) {
            return false
        }
        return true
    }

    void logout() {
        logoutButton.click()
    }

    boolean verifyWelcomeMessage(String username) {
        return usernameLabel.text() == "Welcome Back ${username}!"
    }
}
