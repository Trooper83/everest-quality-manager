package com.manager.quality.everest.test.ui.support.pages.admin.user


import com.manager.quality.everest.test.ui.support.pages.common.BasePage
import geb.module.Checkbox
import geb.module.EmailInput
import geb.module.PasswordInput

class EditUserPage extends BasePage {
    static url = "/user/edit"
    static at = { title == "Edit User" }

    static content = {
        updateButton { $("#updateButton") }
        emailInput { $("#email").module(EmailInput) }
        errorMessage { $(".alert-danger") }
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
        scrollToBottom()
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
