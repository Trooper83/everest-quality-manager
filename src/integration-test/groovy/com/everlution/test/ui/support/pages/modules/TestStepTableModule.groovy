package com.everlution.test.ui.support.pages.modules

class TestStepTableModule extends TableModule {

    static content = {
        addRowButton { $("#btnAddRow") }
    }

    /**
     * adds a test step
     * @param action - the action
     * @param result - the result
     */
    void addTestStep(String action, String result, boolean isFirst = true) {
        if (!isFirst) {
            addRow()
        }
        int index = super.getRowCount() - 1
        $("input[name='steps[${index}].action']") << action
        $("input[name='steps[${index}].result']") << result
    }

    /**
     * adds a row
     */
    void addRow() {
        addRowButton.click()
    }

    /**
     * removes the row at the supplied zero-based index
     * @param index - the zero-based index of the row to remove
     */
    void removeRow(int index) {
        super.getRow(index).find("input[value=Remove]").click()
    }
}
