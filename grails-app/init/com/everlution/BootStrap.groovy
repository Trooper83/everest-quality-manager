package com.everlution

class BootStrap {

    def init = { servletContext ->
        environments {
            test {

            }
            development {
                new TestCase(name: "Everest", description: "").save()
            }
        }
    }

    def destroy = {
    }
}
