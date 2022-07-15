package com.everlution.test.ui.support.pages.admin.user

import com.everlution.test.ui.support.pages.common.BasePage
import geb.module.Checkbox
import geb.module.EmailInput
import geb.module.PasswordInput
import geb.module.TextInput

class EditUserPage extends BasePage {
    static url = "/user/edit"
    static at = { title == "Edit User" }

    static content = {
        updateButton { $("[data-test-id=edit-update-button]") }
        emailInput { $("#email").module(EmailInput) }
        errorMessage { $("ul.errors") }
        passwordInput { $("#password").module(PasswordInput) }
        rolesTabButton { $("[aria-controls='tab-roles']") }
    }

    /**
     * determines if roles are selected
     */
    boolean areRolesSelected(List<String> roles) {
        for(role in roles) {
            def ele = $("#${role}").module(Checkbox)
            if(!ele.checked) {
                return false
            }
        }
        return true
    }

    /**
     * determines if statuses are selected
     */
    boolean areStatusesSelected(List<String> statuses) {
        for(status in statuses) {
            def ele = $("#${status}").module(Checkbox)
            if(!ele.checked) {
                return false
            }
        }
        return true
    }

    /**
     * edits a person, to not edit information of inputs [email, password] pass empty string,
     * for statuses and roles pass an empty list
     */
    void editPerson(String email, String password, List<String> statuses, List<String> roles) {
        if(email) {
            emailInput.text = email
        }
        if(password) {
            passwordInput.text = password
        }
        editStatuses(statuses)
        editRoles(roles)
        updateButton.click()
    }

    /**
     * edits selected roles
     */
    void editRoles(List<String> roles) {
        roles.each {
            $("#${it}").click()
        }
    }

    /**
     * edits selected statuses
     * @param statuses
     */
    void editStatuses(List<String> statuses) {
        statuses.each {
            $("#${it}").click()
        }
    }
}
