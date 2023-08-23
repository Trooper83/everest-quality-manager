package com.everlution

class StepLink {

    Step linkedStep
    Step owner
    Project project
    String relation

    static constraints = {
        linkedStep nullable: false
        relation nullable: false, blank: false, inList: ["Is Child of", "Is Parent of", "Is Sibling of"]
        owner nullable: false
        project nullable: false
    }
}
