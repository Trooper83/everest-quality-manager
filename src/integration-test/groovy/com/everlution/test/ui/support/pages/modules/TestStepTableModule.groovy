package com.everlution.test.ui.support.pages.modules

class TestStepTableModule extends TableModule {

    static content = {
        addRowButton { $("#btnAddRow") }
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
