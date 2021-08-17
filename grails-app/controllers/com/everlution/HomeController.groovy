package com.everlution

import grails.plugin.springsecurity.annotation.Secured

class HomeController {

    @Secured("ROLE_READ_ONLY")
    def index() {
        render(view: "index")
    }
}
