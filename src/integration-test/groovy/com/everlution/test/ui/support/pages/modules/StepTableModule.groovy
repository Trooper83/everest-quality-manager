package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.navigator.Navigator
import org.openqa.selenium.Keys

class StepTableModule extends Module {

    static content = {
        addRowButton { $("#btnAddRow") }
        appendStepsButton { $("#btnAppendSteps") }
        builderStepRows(required: false) { $('#builderSteps div.row') }
        builderTab { $("#builder-tab") }
        currentStepName(required: false) { $("#currentStep p") }
        freeFormTab { $("#free-form-tab") }
        noSuggestedStepsText { $("#noStepsFound") }
        searchInput { $("#search") }
        searchResult { text -> $(".search-results-menu-item", text: text) }
        searchResults { $('.search-results-menu-item') }
        searchResultsMenu { $('#search-results') }
        searchStepsBtn { $('#searchStepBtn') }
        stepRows(required: false) { $('#stepsTableContent div.row') }
        suggestedStep(required: false) { text -> $("#suggestedSteps div.card-body p", text: text) }
        suggestedSteps(required: false) { $("#suggestedSteps div.card") }
    }

    /**
     * adds a builder step
     */
    void addBuilderStep(String text, boolean displayModal = true) {
        if (displayModal) {
            displaySearchStepsModal()
        }
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            searchInput << s
        }
        waitFor {
            searchResultsMenu.displayed
            searchResults.size() > 0
        }
        searchResult(text).first().click()
        waitFor {
            !searchResultsMenu.displayed
        }
        sleep(500)
    }

    /**
     * adds a step
     * @param action - the action
     * @param result - the data
     * @param result - the result
     */
    void addStep(String action, String data, String result) {
        addRow()
        int index = getStepsCount() - 1
        $("textarea[name='steps[${index}].act']").value(action)
        $("textarea[name='steps[${index}].data']").value(data)
        $("textarea[name='steps[${index}].result']").value(result)
    }

    /**
     * adds a row
     */
    void addRow() {
        addRowButton.click()
    }

    /**
     * adds builder steps and closes the modal
     */
    void appendBuilderSteps() {
        appendStepsButton.click()
    }

    /**
     * displays the search steps modal
     */
    void displaySearchStepsModal() {
        searchStepsBtn.click()
        waitFor {
            searchInput.displayed
        }
    }

    /**
     * edits a test step
     * @param index - the index of the row to edit
     * @param action - the updated action value
     * @param result - the updated result value
     */
    void editTestStep(int index, String action, String data, String result) {
        def row = getStep(index).find("textarea")
        row[0].value(action)
        row[1].value(data)
        row[2].value(result)
    }

    /**
     * gets the text of the current step
     */
    String getCurrentBuilderStepName() {
        return currentStepName.text()
    }

    /**
     * determines if a builder steps hidden input is displayed
     * this will fail a test if it is not found at the index
     */
    boolean isBuilderStepHiddenInputDisplayed(int index) {
        def s = getBuilderStep(index).find('input[data-name=hiddenId]')
        assert s.size() == 1 //verify one is found
        return s.displayed
    }

    /**
     * determines if a removed builder steps hidden input is present
     * this will fail a test if one is not found
     */
    boolean isStepHiddenInputDisplayed() {
        def removed = $('input[data-test-id=step-removed-input]')
        assert removed.size() == 1 //verify one is found
        return removed.displayed
    }

    /**
     * determines if a suggested step is displayed
     */
    boolean isSuggestedStepDisplayed(String name) {
        return suggestedStep(name).size() == 1
    }

    /**
     * gets a single builder step by index
     */
    Navigator getBuilderStep(int index) {
        Navigator step = builderStepRows[index]
        if (step.isEmpty()) {
            throw new IllegalArgumentException("Step at index ${index} not found")
        }
        return step
    }

    /**
     * gets a single step by index
     */
    Navigator getStep(int index) {
        Navigator step = stepRows[index]
        if (step.isEmpty()) {
            throw new IllegalArgumentException("Step at index ${index} not found")
        }
        return step
    }

    /**
     * gets the number of builder steps
     */
    int getBuilderStepsCount() {
        return builderStepRows.size()
    }

    /**
     * gets the number of steps
     */
    int getStepsCount() {
        return stepRows.size()
    }

    /**
     * gets the number of suggested steps
     */
    int getSuggestedStepsCount() {
        return suggestedSteps.size()
    }

    /**
     * determines if a builder row with the specified data is displayed
     * @param action
     * @param data
     * @param result
     * @return - true a row contains the action, data and result values,
     * false if a row with the action, data and result is not found
     */
    boolean isBuilderRowDisplayed(String action, String data, String result) {
        for(row in builderStepRows) {
            def d = row.find("textarea")
            if(d[0].text() == action & d[1].text() == data & d[2].text() == result) {
                return true
            }
        }
        return false
    }

    /**
     * determines if a row with the specified data is displayed
     * @param action
     * @param data
     * @param result
     * @return - true a row contains all the supplied values,
     * false if a row is not found
     */
    boolean isRowDisplayed(String action, String data, String result) {
        for(row in stepRows) {
            def d = row.find("textarea")
            if(d[0].text() == action & d[1].text() == data & d[2].text() == result) {
                return true
            }
        }
        return false
    }

    /**
     * removes the builder row at the supplied zero-based index
     * @param index - the zero-based index of the row to remove
     */
    void removeBuilderRow(int index) {
        getBuilderStep(index).find("input[value=Remove]").click()
    }

    /**
     * removes the row at the supplied zero-based index
     * @param index - the zero-based index of the row to remove
     */
    void removeRow(int index) {
        getStep(index).find("input[value=Remove]").click()
    }

    /**
     * selects a suggested step
     */
    void selectSuggestedStep() {
        suggestedSteps.first().click()
    }

    /**
     * selects a suggested step
     */
    void selectSuggestedStep(String name) {
        suggestedSteps(name).click()
    }
}
