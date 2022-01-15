package com.everlution.test.ui.support.pages.testgroup

import com.everlution.test.ui.support.pages.common.CreatePage
import com.github.javafaker.Faker
import geb.module.Select

class CreateTestGroupPage extends CreatePage {
    static url = "/testGroup/create"
    static at = { title == "Create TestGroup" }

    static content = {
        nameInput { $("#name") }
        projectOptions { $("#project>option") }
    }

    Select projectSelect() {
        $("#project").module(Select)
    }

    /**
     * creates a generic test group
     */
    void createTestGroup() {
        Faker faker = new Faker()
        projectSelect().selected = "1"
        nameInput << faker.name().title()
        createButton.click()
    }

    /**
     * creates a test group
     * @param name
     * @param project
     */
    void createTestGroup(String name, String project) {
        nameInput << name
        projectSelect().selected = project
        createButton.click()
    }
}
