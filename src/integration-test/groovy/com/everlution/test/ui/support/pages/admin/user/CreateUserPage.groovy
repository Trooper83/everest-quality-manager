package com.everlution.test.ui.support.pages.admin.user

import com.everlution.test.ui.support.pages.common.BasePage
import geb.module.Checkbox

class CreateUserPage extends BasePage {
    static url = "/user/create"
    static at = { title == "Create User" }

    static content = {
        createButton { $("#create") }
        emailInput { $("#email") }
        errorMessage { $("span.s2ui_error") }
        passwordInput { $("#password") }
        rolesTabButton { $("[aria-controls='tab-roles']") }
        userTabButton { $("[aria-controls='tab-userinfo']") }
    }

    /**
     * creates a new person
     */
    void createPerson(String email, String password, List<String> statuses, List<String> roles) {
        emailInput << email
        passwordInput << password
        statuses.each {
            $("#${it}").module(Checkbox).check()
        }
        if(roles.size() > 0) {
            rolesTabButton.click()
            roles.each {
                $("#${it}").module(Checkbox).check()
            }
        }
        createButton.click()
    }
}
