package com.everlution

class StepLink { //TODO: rename this to be generic Link, Relationship etc...

    Step linkedStep
    Step owner
    Project project
    String relation

    static constraints = {
        linkedStep nullable: false
        relation nullable: false, blank: false, inList: [Relationship.IS_CHILD_OF.name, Relationship.IS_PARENT_OF.name,
                                                         Relationship.IS_SIBLING_OF.name]
        owner nullable: false
        project nullable: false
    }
}
