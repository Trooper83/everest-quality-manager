package com.everlution.test.ui.support.pages.modules

class StepTableModule extends TableModule {

    static content = {
        addRowButton { $("#btnAddRow") }
    }

    /**
     * adds a step
     * @param action - the action
     * @param result - the result
     */
    void addStep(String action, String result) {
        addRow()
        int index = getRowCount() - 1
        $("input[name='steps[${index}].action']").value(action)
        $("input[name='steps[${index}].result']").value(result)
    }

    /**
     * adds a row
     */
    void addRow() {
        addRowButton.click()
    }

    /**
     * edits a test step
     * @param index - the index of the row to edit
     * @param action - the updated action value
     * @param result - the updated result value
     */
    void editTestStep(int index, String action, String result) {
        def data = getRow(index).find("td>input")
        data[1].value(action)
        data[2].value(result)
    }

    /**
     * determines if a row with the specified data is displayed
     * @param action
     * @param result
     * @return - true a row contains both the action and result values,
     * false if a row with the action and result is not found
     */
    boolean isRowDisplayed(String action, String result) {
        for(row in tableRows) {
            def data = row.find("td")
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
        getRow(index).find("input[value=Remove]").click()
    }
}
