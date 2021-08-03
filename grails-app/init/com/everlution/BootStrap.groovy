package com.everlution

class BootStrap {

    def init = { servletContext ->
        new TestCase(name:"Everest", description: "").save()
    }
    def destroy = {
    }
}
