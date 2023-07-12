package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.StepTableModule

class ShowBugPage extends ShowPage {
    static url = "/bug/show"
    static at = { title == "Bug Details" }

    static content = {
        actualValue { $("#actual") }
        areaValue { $("#area") }
        creatorValue { $("#creator") }
        descriptionValue { $("#description") }
        environmentsList { $("#environments") }
        expectedValue { $("#expected") }
        nameValue { $("#name") }
        platformValue { $("#platform") }
        projectValue { $("#project") }
        statusValue { $("#status") }
        steps(required: false) { $("#steps div.row") }
    }

    /**
     * determines if an area is displayed
     * @param names - name of the area to check
     */
    boolean areEnvironmentsDisplayed(List<String> names) {
        return environmentsList.find("div div p")*.text().containsAll(names)
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
