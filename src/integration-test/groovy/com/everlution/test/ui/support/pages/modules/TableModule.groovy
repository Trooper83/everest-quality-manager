package com.everlution.test.ui.support.pages.modules

import geb.Module

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
        return tableHeaders.collect({ h -> h.text() })
    }
}
