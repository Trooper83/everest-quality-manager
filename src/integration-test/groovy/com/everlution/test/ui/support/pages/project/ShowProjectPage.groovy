package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.SideBarModule

class ShowProjectPage extends BasePage {
    static url = "/show"
    static at = { title == "Project Details" }

    static content = {
        areasList { $("#areas") }
        codeValue { $("#code") }
        deleteLink(required: false) { $("[data-test-id=show-delete-link]") }
        editLink(required: false) { $("[data-test-id=show-edit-link]") }
        environmentsList { $("#environments") }
        errorsMessage { $(".alert-danger") }
        nameValue { $("#name") }
        sideBar { module SideBarModule }
        statusMessage { $("div.alert-primary") }
    }

    /**
     * clicks the delete link
     */
    void deleteProject() {
        withConfirm(true) { deleteLink.click() }
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
        return areasList.find("p")*.text().contains(name)
    }

    /**
     * determines if an environment is displayed
     * @param name - name of the env to check
     */
    boolean isEnvironmentDisplayed(String name) {
        return environmentsList.find("p")*.text().contains(name)
    }
}
