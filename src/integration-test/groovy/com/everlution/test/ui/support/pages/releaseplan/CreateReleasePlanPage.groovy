package com.everlution.test.ui.support.pages.releaseplan

import com.everlution.test.ui.support.pages.common.CreatePage
import com.github.javafaker.Faker

class CreateReleasePlanPage extends CreatePage {
    static at = { title == "Create ReleasePlan" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/releasePlan/create"
    }

    static content = {
        nameInput { $("#name") }
    }

    /**
     * creates a generic plan
     */
    void createReleasePlan() {
        Faker faker = new Faker()
        nameInput << faker.name().title()
        createButton.click()
    }

    /**
     * creates a plan with the supplied data
     */
    void createReleasePlan(String name) {
        nameInput << name
        createButton.click()
    }

    /**
     * Gets the labels for all fields displayed on the page
     * @return - a list of field names
     */
    List<String> getModalFields() {
        return fieldLabels*.text()
    }
}
