package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.ProjectNavModule
import com.everlution.test.ui.support.pages.modules.SideBarModule

class ShowProjectPage extends BasePage {
    static url = "/show"
    static at = { title == "Show Project" }

    static content = {
        areasList { $("#areas") }
        codeValue { $("#code") }
        deleteLink(required: false) { $("[data-test-id=home-delete-link]") }
        editLink(required: false) { $("[data-test-id=home-edit-link]") }
        environmentsList { $("#environments") }
        errorsMessage { $("ul.errors") }
        fieldLabels { $("ol.property-list>li>span") }
        nameValue { $("#name") }
        projectNavButtons { module ProjectNavModule }
        sideBar { module SideBarModule }
        statusMessage { $("div.message") }
    }

    /**
     * clicks the delete link
     */
    void deleteProject() {
        withConfirm(true) { deleteLink.click() }
    }

    /**
     * Gets the labels for all fields displayed on the page
     * @return - a list of field names
     */
    List<String> getFields() {
        return fieldLabels*.text()
    }

    /**
     * clicks the edit link
     */
    void goToEdit() {
        editLink.click()
    }

    /**
     * determines if an area is displayed
     * @param name - name of the area to check
     */
    boolean isAreaDisplayed(String name) {
        return areasList.find("div")*.text().contains(name)
    }

    /**
     * determines if an environment is displayed
     * @param name - name of the env to check
     */
    boolean isEnvironmentDisplayed(String name) {
        return environmentsList.find("div")*.text().contains(name)
    }
}
