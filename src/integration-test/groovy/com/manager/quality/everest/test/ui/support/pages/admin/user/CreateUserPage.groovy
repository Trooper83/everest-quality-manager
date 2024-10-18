package com.manager.quality.everest.test.ui.support.pages.admin.user


import com.manager.quality.everest.test.ui.support.pages.common.BasePage
import geb.module.Checkbox
import geb.module.EmailInput
import geb.module.PasswordInput

class CreateUserPage extends BasePage {
    static url = "/user/create"
    static at = { title == "Create User" }

    static content = {
        createButton { $("#create") }
        emailInput { $("#email").module(EmailInput) }
        errorMessage { $(".alert-danger") }
        passwordInput { $("#password").module(PasswordInput) }
        statusMessage { $(".alert-primary") }
    }

    /**
     * creates a new person
     */
    void createPerson(String email, String password, List<String> statuses, List<String> roles) {
        emailInput.text = email
        passwordInput.text = password
        statuses.each {
            $("#${it}").module(Checkbox).check()
        }
        roles.each {
            $("#${it}").module(Checkbox).check()
        }
        scrollToBottom()
        createButton.click()
    }
}
