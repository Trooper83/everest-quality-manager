package com.everlution

class StepLink {

    Step child
    Step parent
    Project project

    static constraints = {
        child nullable: false
        parent nullable: false
        project nullable: false
    }
}
