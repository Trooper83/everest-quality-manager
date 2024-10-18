package com.manager.quality.everest.test.ui.support.pages.testgroup

import com.manager.quality.everest.test.ui.support.pages.common.CreatePage
import com.github.javafaker.Faker

class CreateTestGroupPage extends CreatePage {
    static at = { title == "Create TestGroup" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/testGroup/create"
    }

    static content = {
        nameInput { $("#name") }
    }

    /**
     * creates a generic test group
     */
    void createTestGroup() {
        Faker faker = new Faker()
        nameInput << faker.name().title()
        createButton.click()
    }

    /**
     * creates a test group
     * @param name
     * @param project
     */
    void createTestGroup(String name) {
        nameInput << name
        createButton.click()
    }
}
