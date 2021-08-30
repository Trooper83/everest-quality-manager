package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.navigator.Navigator

class TableModule extends Module {
    static content = {
        tableHeaders { $("thead th") }
        tableRows { $("tbody tr") }
    }

    /**
     * gets the header names
     * @return - list of header names
     */
    List<String> getHeaders() {
        return tableHeaders*.text()
    }

    /**
     * gets the row at the specified index
     * @param index - the zero-based index
     * @return - the row
     * @throws - IllegalArgumentException if the row is not found
     */
    Navigator getRow(int index) {
        Navigator row = tableRows[index]
        if (row.isEmpty()) {
            throw new IllegalArgumentException("Row at index ${index} not found")
        }
        return row
    }

    /**
     * gets the number of rows
     * @return
     */
    int getRowCount() {
        tableRows.size()
    }
}
