package com.everlution.test.ui.support.pages.project

import com.everlution.test.ui.support.pages.common.BasePage
import com.everlution.test.ui.support.pages.modules.SideBarModule
import com.everlution.test.ui.support.pages.modules.TableModule

class ProjectHomePage extends BasePage {
    static at = { title == "Project Home" }
    static convertToPath(Long projectId) {
        "/project/home/${projectId}"
    }

    static content = {
        errorsMessage { $("ul.errors") }
        inProgressPlansCard { $("#inProgressReleasePlanCard") }
        inProgressPlanLinks { inProgressPlansCard.find("a") }
        inProgressPlansNotFoundText { inProgressPlansCard.find('[data-test-id="notFoundText"]') }
        recentBugsTable { module TableModule }
        releasedPlansCard { $("#releasedReleasePlanCard") }
        releasedPlanLinks { releasedPlansCard.find("a") }
        releasedPlansNotFoundText { releasedPlansCard.find('[data-test-id="notFoundText"]') }
        sideBar { module SideBarModule }
        statusMessage { $("div.message") }
        viewButton(required: false) { $("#viewButton") }
    }

    void goToInProgressPlan() {
        inProgressPlanLinks.first().click()
    }

    void goToReleasedPlan() {
        releasedPlanLinks.first().click()
    }
}
