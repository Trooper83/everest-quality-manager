package com.everlution.domains

class TestIteration {

    Date dateCreated
    Date dateExecuted
    Date lastUpdated
    String name
    String notes
    Person person
    String result
    TestCase testCase
    TestCycle testCycle
    List steps
    String verify

    static belongsTo = [ TestCycle ]

    static hasMany = [ steps: IterationStep ]

    static mapping = {
        person cascade: "none"
        steps cascade: "all-delete-orphan"
        testCase cascade: "none"
        testCycle cascade: "none"
    }

    static constraints = {
        dateExecuted nullable: true
        name blank: false, maxSize: 255, nullable: false
        notes blank: true, maxSize: 1000, nullable: true
        person nullable: true
        result blank: false, nullable: false, inList: ["ToDo", "Passed", "Failed"]
        steps nullable: false
        testCase nullable: false
        testCycle nullable: false
        verify nullable: true, blank: true, maxSize: 500
    }
}