package com.everlution.test.ui.support.pages.releaseplan

import com.everlution.test.ui.support.pages.common.CreatePage
import com.github.javafaker.Faker
import geb.module.Select

class CreateReleasePlanPage extends CreatePage {
    static at = { title == "Create ReleasePlan" }

    static String convertToPath(Long projectId) {
        "/project/${projectId}/releasePlan/create"
    }

    static content = {
        nameInput { $("#name") }
        notesInput { $("#notes") }
        plannedDateDaySelect { $("#plannedDate_day").module(Select) }
        plannedDateMonthSelect { $("#plannedDate_month").module(Select) }
        plannedDateYearSelect { $("#plannedDate_year").module(Select) }
        releaseDateDaySelect { $("#releaseDate_day").module(Select) }
        releaseDateMonthSelect { $("#releaseDate_month").module(Select) }
        releaseDateYearSelect { $("#releaseDate_year").module(Select) }
        statusSelect { $("#status").module(Select) }
        statusSelectOptions { $("#status > option")}
    }

    /**
     * creates a generic plan
     */
    void createReleasePlan() {
        Faker faker = new Faker()
        nameInput << faker.name().title()
        createButton.click()
    }

    /**
     * creates a plan with the supplied data
     */
    void createReleasePlan(String name, String status, String plannedDate, String releaseDate, String notes) {
        def plannedDateList = plannedDate.replace(',', '').split(' ')
        def releaseDateList = releaseDate.replace(',', '').split(' ')
        nameInput << name
        statusSelect.selected = status
        plannedDateMonthSelect.selected = plannedDateList[0]
        plannedDateDaySelect.selected = plannedDateList[1]
        plannedDateYearSelect.selected = plannedDateList[2]
        releaseDateDaySelect.selected = releaseDateList[1]
        releaseDateMonthSelect.selected = releaseDateList[0]
        releaseDateYearSelect.selected = releaseDateList[2]
        notesInput << notes
        createButton.click()
    }
}
