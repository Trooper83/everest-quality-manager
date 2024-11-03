package com.manager.quality.everest.domains

class TestCycle {

    Date dateCreated
    Environment environ
    String name
    Platform platform
    ReleasePlan releasePlan
    Collection testCaseIds
    Collection testIterations

    static hasMany = [ testCaseIds: Long, testIterations: TestIteration ]

    static belongsTo = [ ReleasePlan ]

    static mapping = {
        environ cascade: "none"
        releasePlan cascade: "none"
        platform cascade: "none"
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
            val.id in ids
        }
        name nullable: false, blank: false, maxSize: 500
        platform nullable: true, validator: { val, TestCycle obj ->
            if(val == null) {
                return
            }
            if(obj.releasePlan.project == null || obj.releasePlan.project.platforms == null) {
                return false
            }
            def ids = obj.releasePlan.project.platforms*.id
            val.id in ids
        }
        releasePlan nullable: false
        testCaseIds nullable: true
        testIterations nullable: true
    }
}
