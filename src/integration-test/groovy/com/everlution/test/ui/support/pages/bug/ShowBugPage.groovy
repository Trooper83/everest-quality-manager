package com.everlution.test.ui.support.pages.bug

import com.everlution.test.ui.support.pages.common.ShowPage
import com.everlution.test.ui.support.pages.modules.StepTableModule

class ShowBugPage extends ShowPage {
    static url = "/bug/show"
    static at = { title == "Show Bug" }

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
        stepsTable { module StepTableModule }
    }

    /**
     * determines if an area is displayed
     * @param names - name of the area to check
     */
    boolean areEnvironmentsDisplayed(List<String> names) {
        return environmentsList.find("div")*.text().containsAll(names)
    }
}
