package com.manager.quality.everest.domains

class TestIterationResult {

    Date dateCreated
    String notes
    Person person
    String result

    static belongsTo = [ TestIteration ]

    static mapping = {
        person cascade: "none"
    }

    static constraints = {
        dateCreated nullable: true
        notes nullable: true, blank: true, maxSize: 1000
        person nullable: false
        result blank: false, nullable: false, inList: ["FAILED", "PASSED", "SKIPPED"]
    }
}
