package com.everlution.domains

class TestIteration {

    Date dateCreated
    Date lastUpdated
    String name
    TestCase testCase
    TestCycle testCycle
    List steps
    List results
    String verify

    static belongsTo = [ TestCycle ]

    static hasMany = [ steps: IterationStep, results: TestIterationResult ]

    static mapping = {
        results cascade: "all-delete-orphan"
        steps cascade: "all-delete-orphan"
        testCase cascade: "none"
        testCycle cascade: "none"
    }

    static constraints = {
        name blank: false, maxSize: 255, nullable: false
        results nullable: true
        steps nullable: false
        testCase nullable: false
        testCycle nullable: false
        verify nullable: true, blank: true, maxSize: 500
    }
}
