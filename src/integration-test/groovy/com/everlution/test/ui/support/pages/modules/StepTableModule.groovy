package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.navigator.Navigator
import org.openqa.selenium.Keys

class StepTableModule extends Module {

    static content = {
        addRowButton { $("#btnAddRow") }
        stepRows(required: false) { $('#stepsTableContent div.row') }
    }

    /**
     * adds a step
     * @param action - the action
     * @param result - the result
     */
    void addStep(String action, String result) {
        addRow()
        int index = getStepsCount() - 1
        $("textarea[name='steps[${index}].act']").value(action)
        $("textarea[name='steps[${index}].result']").value(result)
    }

    /**
     * adds a row
     */
    void addRow() {
        addRowButton.click()
    }

    /**
     * adds a new row with ALT+n
     */
    void addRowHotKey() {
        addRowButton << Keys.chord(Keys.ALT, 'n')
    }

    /**
     * edits a test step
     * @param index - the index of the row to edit
     * @param action - the updated action value
     * @param result - the updated result value
     */
    void editTestStep(int index, String action, String result) {
        def data = getStep(index).find("textarea")
        data[0].value(action)
        data[1].value(result)
    }

    Navigator getStep(int index) {
        Navigator step = stepRows[index]
        if (step.isEmpty()) {
            throw new IllegalArgumentException("Step at index ${index} not found")
        }
        return step
    }

    /**
     * gets the number of steps
     */
    int getStepsCount() {
        return stepRows.size()
    }

    /**
     * determines if a row with the specified data is displayed
     * @param action
     * @param result
     * @return - true a row contains both the action and result values,
     * false if a row with the action and result is not found
     */
    boolean isRowDisplayed(String action, String result) {
        for(row in stepRows) {
            def data = row.find("textarea")
            if(data[0].text() == action & data[1].text() == result) {
                return true
            }
        }
        return false
    }

    /**
     * removes the row at the supplied zero-based index
     * @param index - the zero-based index of the row to remove
     */
    void removeRow(int index) {
        getStep(index).find("input[value=Remove]").click()
    }
}
