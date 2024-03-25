package com.everlution.domains

class TestCycle {

    Date dateCreated
    Environment environ
    String name
    String platform
    ReleasePlan releasePlan
    Collection testCaseIds
    Collection testIterations

    static hasMany = [ testCaseIds: Long, testIterations: TestIteration ]

    static belongsTo = [ ReleasePlan ]

    static mapping = {
        testIterations cascade: "all-delete-orphan"
    }

    static constraints = {
        environ nullable: true
        name nullable: false, blank: false, maxSize: 500
        platform blank: true, nullable: true, inList: ["Android", "iOS", "Web"]
        releasePlan nullable: false
        testCaseIds nullable: true
        testIterations nullable: true
    }
}
