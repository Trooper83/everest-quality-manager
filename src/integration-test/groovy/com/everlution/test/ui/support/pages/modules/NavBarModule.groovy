package com.everlution.test.ui.support.pages.modules

import geb.Module

class NavBarModule extends Module {

    static content = {
        adminButton(required: false) { $("#adminButton") }
        adminDropDownMenu { $("#adminDropDownMenu") }
        domainDropDown { $("#domainDropDown") }
        domainDropDownMenu { $("#domainDropDownMenu") }
        logoutButton { $("[data-test-id=main-logout-button]") }
        profileDropDown { $("#profileDropDown") }
        profileLink { $("#profileLink") }
        projectsLink { $("#projectsLink") }
    }

    /**
     * clicks and admin option
     */
    void goToAdminPage(String page) {
        adminButton.click()
        adminDropDownMenu.find('a', text: page).click()
    }

    /**
     * clicks the profile link
     */
    void goToProfile() {
        profileDropDown.click()
        profileLink.click()
    }

    /**
     * clicks the projects link
     */
    void goToProjects() {
        projectsLink.click()
    }

    /**
     * goes to a project domain
     */
    void goToProjectDomain(String domain) {
        domainDropDown.click()
        domainDropDownMenu.find('a', text: domain).click()
    }

    /**
     * determines if an option is displayed
     */
    boolean isOptionDisplayed(String option) {
        if(!adminDropDownMenu.displayed) { adminButton.click() }
        def count = adminDropDownMenu.find('a', text: option).size()
        return count == 1
    }

    /**
     * logs the user out of the app
     */
    void logout() {
        profileDropDown.click()
        logoutButton.click()
    }
}
