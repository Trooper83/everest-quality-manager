package com.everlution.test.ui.support.pages.step

import com.everlution.test.ui.support.pages.common.ShowPage

class ShowStepPage extends ShowPage {

    static at = { title == "Step Details" }

    static String convertToPath(Long projectId, Long stepId) {
        "/project/${projectId}/step/show/${stepId}"
    }

    static content = {
        actionValue { $('#act') }
        nameValue { $('#name') }
        resultValue { $('#result') }
    }

    /**
     * determines if a linked step is present
     */
    boolean isLinkDisplayed(String name, String relation) {
       def links = $("#${relation}").find('.card')
        for (def link : links) {
            if (link.text() == name) {
                return true
            }
        }
        return false
    }
}
