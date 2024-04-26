package com.everlution.domains

class TestIteration {

    Date dateCreated
    Date lastExecuted
    Date lastUpdated
    String lastResult
    String name
    Person lastExecutedBy
    TestCase testCase
    TestCycle testCycle
    List steps
    List results
    String verify

    static belongsTo = [ TestCycle ]

    static hasMany = [ steps: IterationStep, results: TestIterationResult ]

    static mapping = {
        lastExecutedBy cascade: "none"
        results cascade: "all-delete-orphan"
        steps cascade: "all-delete-orphan"
        testCase cascade: "none"
        testCycle cascade: "none"
    }

    static constraints = {
        lastExecuted nullable: true
        lastExecutedBy nullable: true
        lastResult blank: true, nullable: true, inList: ["FAILED", "PASSED", "SKIPPED"]
        name blank: false, maxSize: 255, nullable: false
        results nullable: true
        steps nullable: false
        testCase nullable: false
        testCycle nullable: false
        verify nullable: true, blank: true, maxSize: 500
    }
}
