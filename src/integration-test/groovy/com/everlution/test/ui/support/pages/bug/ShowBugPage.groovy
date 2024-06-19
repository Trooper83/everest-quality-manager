package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.StepTableModule

class ShowBugPage extends ShowPage {
    static at = { title == "Bug Details" }
    static convertToPath(Long projectId, Long id) {
        "/project/${projectId}/bug/show/${id}"
    }

    static content = {
        actualValue { $("#actual") }
        areaValue { $("#area") }
        creatorValue { $("#creator") }
        descriptionValue { $("#description") }
        environmentsList { $("#environments") }
        expectedValue { $("#expected") }
        nameValue { $("#name") }
        notesValue { $("#notes") }
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
