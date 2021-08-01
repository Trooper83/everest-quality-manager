package com.everlution

class BootStrap {

    def init = { servletContext ->
        new TestCase(name:"Everest").save()
    }
    def destroy = {
    }
}
