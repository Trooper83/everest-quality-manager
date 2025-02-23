package com.manager.quality.everest.test.ui.support.pages.testcase

import com.manager.quality.everest.test.ui.support.pages.common.ShowPage
import com.manager.quality.everest.test.ui.support.pages.modules.StepTableModule

class ShowTestCasePage extends ShowPage {
    static at = { title == "TestCase Details" }
    static convertToPath(Long projectId, Long id) {
        "/project/${projectId}/testCase/show/${id}"
    }

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
        verifyValue { $ ("#verify") }
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
     */
    boolean isStepsRowDisplayed(String action, String data, String result) {
        for(row in steps) {
            def d = row.find("p")
            if(d[0].text() == action & d[1].text() == data & d[2].text() == result) {
                return true
            }
        }
        return false
    }
}
