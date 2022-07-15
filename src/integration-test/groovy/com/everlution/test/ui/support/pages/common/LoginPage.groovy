package com.everlution.test.ui.support.pages.common

import geb.module.EmailInput
import geb.module.PasswordInput

class LoginPage extends BasePage {
    static url = "/login/auth"
    static at = { title == "Login" }

    static content = {
        loginButton { $(id: "submit") }
        loginFailureMessage { $("div.alert")}
        passwordTextField { $(id: "password").module(PasswordInput) }
        emailTextField { $(id: "email").module(EmailInput) }
    }

    /**
     * logs into the app
     * @param username
     * @param password
     */
    void login(String username, String password) {
        emailTextField.text = username
        passwordTextField.text = password
        loginButton.click()
    }
}
