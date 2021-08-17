package com.everlution.test.ui.support.pages

class LoginPage extends BasePage {
    static url = "/auth/login"
    static at = { title == "Login" }

    static content = {
        loginButton { $(id: "submit") }
        passwordTextField { $(id: "password") }
        usernameTextField { $(id: "username") }
    }

    void login(String username, String password) {
        usernameTextField << username
        passwordTextField << password
        loginButton.click()
    }
}
