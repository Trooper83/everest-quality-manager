package com.manager.quality.everest.test.ui.support.pages.releaseplan

import com.manager.quality.everest.test.ui.support.pages.common.EditPage
import geb.module.Select

class EditReleasePlanPage extends EditPage {
    static at = { title == "Edit ReleasePlan" }

    static String convertToPath(Long projectId, Long id) {
        "/project/${projectId}/releasePlan/edit/${id}"
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
     * edits a plan with the supplied data
     */
    void editReleasePlan(String name, String status, String plannedDate, String releaseDate, String notes) {
        nameInput = name
        def plannedDateList = plannedDate.replace(',', '').split(' ')
        def releaseDateList = releaseDate.replace(',', '').split(' ')
        statusSelect.selected = status
        plannedDateMonthSelect.selected = plannedDateList[0]
        plannedDateDaySelect.selected = plannedDateList[1]
        plannedDateYearSelect.selected = plannedDateList[2]
        releaseDateDaySelect.selected = releaseDateList[1]
        releaseDateMonthSelect.selected = releaseDateList[0]
        releaseDateYearSelect.selected = releaseDateList[2]
        notesInput = notes
        updateButton.click()
    }
}
