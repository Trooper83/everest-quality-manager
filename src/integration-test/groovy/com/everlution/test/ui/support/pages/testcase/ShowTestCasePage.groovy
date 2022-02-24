package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.StepTableModule

class ShowTestCasePage extends ShowPage {
    static url = "/testCase/show"
    static at = { title == "Show TestCase" }

    static content = {
        areaValue { $("#area") }
        creatorValue { $("#creator") }
        descriptionValue { $("#description") }
        environmentsList { $("#environments") }
        executionMethodValue { $("#executionMethod") }
        nameValue { $("#name") }
        platformValue { $("#platform") }
        projectValue { $("#project") }
        testGroupsList { $("#testGroups") }
        testStepTable { module StepTableModule }
        typeValue { $ ("#type") }
    }

    /**
     * determines if an environment is displayed
     * @param names - name of the environment to check
     */
    boolean areEnvironmentsDisplayed(List<String> names) {
        return environmentsList.find("div")*.text().containsAll(names)
    }

    /**
     * determines if test groups are displayed
     * @param names - name of the test group to check
     */
    boolean areTestGroupsDisplayed(List<String> names) {
        return testGroupsList.find("div")*.text().containsAll(names)
    }
}
