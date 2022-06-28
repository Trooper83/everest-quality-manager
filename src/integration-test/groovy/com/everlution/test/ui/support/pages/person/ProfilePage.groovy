package com.everlution.test.ui.support.pages.person

import com.everlution.test.ui.support.pages.common.BasePage
import geb.module.PasswordInput
import geb.module.TextInput

class ProfilePage extends BasePage {
    static url = "/user/profile"
    static at = { title == "Profile" }

    static content = {
        confirmPasswordInput { $("#confirmPassword").module(PasswordInput) }
        emailInput { $("#email").module(TextInput) }
        errorMessage { $("ul.errors") }
        passwordInput { $("#password").module(PasswordInput) }
        statusMessage { $("div.message") }
        updateButton { $("#updateButton") }
    }

    void updatePassword(String password) {
        passwordInput.text = password
        confirmPasswordInput.text = password
        updateButton.click()
    }
}
