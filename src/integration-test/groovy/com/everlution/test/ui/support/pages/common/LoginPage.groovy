package com.everlution.test.ui.support.pages.common

class LoginPage extends BasePage {
    static url = "/login/auth"
    static at = { title == "Login" }

    static content = {
        loginButton { $(id: "submit") }
        loginFailureMessage { $("div.login_message")}
        passwordTextField { $(id: "password") }
        usernameTextField { $(id: "username") }
    }

    /**
     * logs into the app
     * @param username
     * @param password
     */
    void login(String username, String password) {
        usernameTextField << username
        passwordTextField << password
        loginButton.click()
    }
}
