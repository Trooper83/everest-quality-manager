package com.everlution.test.ui.support.pages.modules

import geb.Module
import geb.navigator.Navigator

class TableModule extends Module {
    static content = {
        paginationGroup { $(".pagination") }
        paginationGroupLink { text -> $(".pagination li a", text: text) }
        tableHeaders { $("thead th") }
        tableRows(required: false) { $("tbody tr") }
    }

    /**
     * clicks a link in a cell
     * @param columnName - name of the column
     * @param rowIndex - index of the row
     */
    void clickCell(String columnName, int rowIndex) {
        int columnIndex = getColumnIndex(columnName)
        def cells = tableRows[rowIndex].find("td")
        cells[columnIndex].find("a").click()
    }

    /**
     * clicks a link in a cell
     * @param columnName - name of the column
     * @param value - value to find
     */
    void clickCell(String columnName, String value) {
        int columnIndex = getColumnIndex(columnName)
        def cells = tableRows.collect( r -> r.find('td')[columnIndex])
        cells.find { c -> c.has('a', text: value) }.find('a').click()
    }

    /**
     * finds the index of column with the supplied name
     * @param columnName - name of the column
     * @return - zero-based index
     * @throws - IllegalArgumentException if column not found
     */
    int getColumnIndex(String columnName) {
        int index = getHeaders().findIndexOf({ it == columnName })
        if (index == -1) {
            throw new IllegalArgumentException("${columnName} column not found")
        }
        return index
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

    /**
     * gets if a value in a column
     */
    String getValueInColumn(int row, String columnName) {
        int columnIndex = getColumnIndex(columnName)
        def value = tableRows[row].find('td')[columnIndex].text()
        return value
    }

    /**
     * clicks a pagination link
     */
    void goToPage(String linkText) {
        paginationGroupLink(linkText).click()
    }

    /**
     * determines if a value is found in a column
     */
    boolean isValueInColumn(String columnName, String value) {
        int columnIndex = getColumnIndex(columnName)
        def cells = tableRows.collect( r -> r.find('td')[columnIndex])
        return cells.any { c -> c.text() == value }
    }
}
