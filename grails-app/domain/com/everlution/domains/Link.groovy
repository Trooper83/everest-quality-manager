package com.everlution.domains

import com.everlution.Relationship

class Link {

    Long linkedId
    Long ownerId
    Project project
    String relation

    static mapping = {
        project cascade: "none"
    }

    static constraints = {
        linkedId nullable: false
        relation nullable: false, blank: false, inList: [Relationship.IS_CHILD_OF.name, Relationship.IS_PARENT_OF.name,
                                                         Relationship.IS_SIBLING_OF.name]
        ownerId nullable: false
        project nullable: false
    }
}
