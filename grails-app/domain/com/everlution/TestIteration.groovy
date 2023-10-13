package com.everlution

class TestIteration {

    Date dateCreated
    Date lastUpdated
    String name
    String notes
    Person person
    String result
    TestCase testCase
    TestCycle testCycle
    List steps

    static belongsTo = [ TestCycle ]

    static hasMany = [ steps: IterationStep ]

    static mapping = {
        person cascade: "none"
        steps cascade: "all-delete-orphan"
        testCase cascade: "none"
        testCycle cascade: "none"
    }

    static constraints = {
        name blank: false, maxSize: 255, nullable: false
        notes blank: true, maxSize: 1000, nullable: true
        person nullable: true
        result blank: false, nullable: false, inList: ["ToDo", "Pass", "Fail"]
        steps nullable: false
        testCase nullable: false
        testCycle nullable: false
    }
}
