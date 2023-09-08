package com.everlution.test.ui.support.pages.step

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.pages.common.CreatePage
import com.everlution.test.ui.support.pages.modules.LinkModule

class CreateStepPage extends CreatePage {

    static at = { title == "Create Step" }
    static String convertToPath(Long projectId) {
        "/project/${projectId}/step/create"
    }

    static content = {
        actionInput { $("#act") }
        resultInput { $("#result") }
        nameInput { $("#name") }
        linkModule { module LinkModule }
    }

    /**
     * creates a generic step
     */
    void createStep() {
        def step = DataFactory.step()
        nameInput = step.name
        actionInput = step.action
        resultInput = step.result
        scrollToBottom()
        createButton.click()
    }

    /**
     * creates a step with the passed in data
     */
    void createStep(String action, String name, String result) {
        nameInput = name
        actionInput = action
        resultInput = result
        scrollToBottom()
        createButton.click()
    }

    /**
     * creates a step with the passed in data
     */
    void createStepWithLink(String action, String name, String result, String linkName, String relation) {
        nameInput = name
        actionInput = action
        resultInput = result
        scrollToBottom()
        linkModule.addLink(linkName, relation)
        scrollToBottom()
        createButton.click()
    }
}
