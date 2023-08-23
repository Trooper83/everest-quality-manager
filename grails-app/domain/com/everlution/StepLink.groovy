package com.everlution

class StepLink {

    Step linkedStep
    Step owner
    Project project
    String relation

    static constraints = {
        linkedStep nullable: false
        relation nullable: false, blank: false, inList: ["CHILD_PARENT", "PARENT_CHILD", "SIBLING"]
        owner nullable: false
        project nullable: false
    }
}
