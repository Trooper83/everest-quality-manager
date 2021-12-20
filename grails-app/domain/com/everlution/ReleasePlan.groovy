package com.everlution

class ReleasePlan {

    Date dateCreated
    String name
    Project project
    Collection testCycles

    static hasMany = [testCycles: TestCycle]

    static mapping = {
        project cascade: "none"
    }

    static constraints = {
        name nullable: false, blank: false, maxSize: 500
        project nullable: false
        testCycles nullable: true
    }
}
