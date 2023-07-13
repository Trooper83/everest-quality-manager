package com.everlution.test.ui.support.pages.testcase

import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.StepTableModule

class ShowTestCasePage extends ShowPage {
    static url = "/testCase/show"
    static at = { title == "TestCase Details" }

    static content = {
        areaValue { $("#area") }
        creatorValue { $("#creator") }
        descriptionValue { $("#description") }
        environmentsList { $("#environments") }
        executionMethodValue { $("#executionMethod") }
        nameValue { $("#name") }
        platformValue { $("#platform") }
        projectValue { $("#project") }
        steps(required: false) { $("#steps div.row") }
        testGroupsList { $("#testGroups") }
        testStepTable { module StepTableModule }
        typeValue { $ ("#type") }
    }

    /**
     * determines if an environment is displayed
     * @param names - name of the environment to check
     */
    boolean areEnvironmentsDisplayed(List<String> names) {
        return environmentsList.find("div div p")*.text().containsAll(names)
    }

    /**
     * determines if test groups are displayed
     * @param names - name of the test group to check
     */
    boolean areTestGroupsDisplayed(List<String> names) {
        return testGroupsList.find("div div p")*.text().containsAll(names)
    }

    /**
     * gets the number of steps
     * @return
     */
    int getStepsCount() {
        return steps.size()
    }

    /**
     * determines if a row with the specified data is displayed
     * @param action
     * @param result
     * @return - true a row contains both the action and result values,
     * false if a row with the action and result is not found
     */
    boolean isStepsRowDisplayed(String action, String result) {
        for(row in steps) {
            def data = row.find("p")
            if(data[0].text() == action & data[1].text() == result) {
                return true
            }
        }
        return false
    }
}
