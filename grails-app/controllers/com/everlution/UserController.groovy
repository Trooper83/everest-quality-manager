package com.everlution

class UserController extends grails.plugin.springsecurity.ui.UserController {

    @Override
    def search() {
        if (!isSearch()) {
            // show the form
            return
        }

        def results = doSearch { ->
            like 'email', delegate
            eqBoolean 'accountExpired', delegate
            eqBoolean 'accountLocked', delegate
            eqBoolean 'enabled', delegate
            eqBoolean 'passwordExpired', delegate
        }

        renderSearch results: results, totalCount: results.totalCount,
                'accountExpired', 'accountLocked', 'enabled', 'passwordExpired', 'email'
    }
}
