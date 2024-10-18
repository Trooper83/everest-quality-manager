package com.manager.quality.everest.test.ui.support.pages.person

import com.manager.quality.everest.test.ui.support.pages.common.BasePage
import geb.module.PasswordInput
import geb.module.TextInput

class ProfilePage extends BasePage {
    static url = "/user/profile"
    static at = { title == "User Profile" }

    static content = {
        confirmPasswordInput { $("#confirmPassword").module(PasswordInput) }
        emailInput { $("#email").module(TextInput) }
        errorMessage { $(".alert-danger") }
        passwordInput { $("#password").module(PasswordInput) }
        statusMessage { $(".alert-primary") }
        updateButton { $("#updateButton") }
    }

    void updatePassword(String password) {
        passwordInput.text = password
        confirmPasswordInput.text = password
        updateButton.click()
        sleep(2000) //need to slow down the test
    }
}
