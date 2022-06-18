package com.everlution.test.ui.support.pages.user

import com.everlution.test.ui.support.pages.common.BasePage

class SearchUserPage extends BasePage {
    static url = "/user/search"
    static at = { title == "User Search" }

    private statusMap = ['true': '1', 'false': '-1', 'either': '0']

    static content = {
        emailInput { $("#email") }
        resultsTable { $("#results") }
        searchButton { $("#searchButton") }
        tableHeaders { resultsTable.find("thead th") }
        tableRows(required: false) { resultsTable.find("tbody tr") }
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
     * gets the number of rows
     * @return
     */
    int getRowCount() {
        tableRows.size()
    }

    /**
     * determines if a value is found in a column
     */
    boolean isValueInColumn(String columnName, String value) {
        int columnIndex = getColumnIndex(columnName)
        def cells = tableRows.collect( r -> r.find('td')[columnIndex])
        return cells.any { c -> c.text() == value }
    }

    /**
     * searches for a user by text and status [true, false or either]
     */
    void search(String text, LinkedHashMap<String, String> statuses) {
        emailInput << text
        statuses.each { key, value ->
            def v = statusMap.get(value)
            $("[name=${key}][value='${v}']").click()
        }
        searchButton.click()
    }
}
