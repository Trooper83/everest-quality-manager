package com.everlution

class TestIteration {

    String name
    String result
    TestCase testCase
    TestCycle testCycle
    List steps

    static belongsTo = [ TestCycle ]

    static hasMany = [ steps: IterationStep ]

    static mapping = {
        steps cascade: "all-delete-orphan"
    }

    static constraints = {
        name blank: false, maxSize: 255, nullable: false
        result blank: false, nullable: false, inList: ["ToDo", "Pass", "Fail"]
        steps nullable: false
        testCase nullable: false
        testCycle nullable: false
    }
}
