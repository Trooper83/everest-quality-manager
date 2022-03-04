package com.everlution

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
        environ nullable: true, validator: { val, TestCycle obj ->
            if(val == null) {
                return
            }
            if(obj.releasePlan.project == null || obj.releasePlan.project.environments == null) {
                return false
            }
            def ids = obj.releasePlan.project.environments*.id
            ids.contains(val.id)
        }
        name nullable: false, blank: false, maxSize: 500
        platform blank: true, nullable: true, inList: ["Android", "iOS", "Web"]
        releasePlan nullable: false
        testCaseIds nullable: true
        testIterations nullable: true
    }
}
