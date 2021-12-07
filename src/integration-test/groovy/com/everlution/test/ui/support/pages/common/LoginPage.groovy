package com.everlution.test.ui.support.pages.common

class LoginPage extends BasePage {
    static url = "/login/auth"
    static at = { title == "Login" }

    static content = {
        loginButton { $(id: "submit") }
        loginFailureMessage { $("div.alert")}
        passwordTextField { $(id: "password") }
        emailTextField { $(id: "email") }
    }

    /**
     * logs into the app
     * @param username
     * @param password
     */
    void login(String username, String password) {
        emailTextField << username
        passwordTextField << password
        loginButton.click()
    }
}
