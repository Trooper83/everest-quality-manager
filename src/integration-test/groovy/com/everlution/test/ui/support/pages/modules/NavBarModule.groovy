package com.everlution.test.ui.support.pages.modules

import geb.Module

class NavBarModule extends Module {

    static content = {
        loginLink { $("data-test-id=auth-login-link") }
    }
}
