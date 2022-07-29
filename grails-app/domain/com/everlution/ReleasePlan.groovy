package com.everlution

class ReleasePlan {

    Date dateCreated
    String name
    Date plannedDate
    Project project
    Date releaseDate
    String status
    Collection testCycles

    static hasMany = [ testCycles: TestCycle ]

    static mapping = {
        project cascade: "none"
        testCycles cascade: "all-delete-orphan"
    }

    static constraints = {
        name nullable: false, blank: false, maxSize: 500
        plannedDate nullable: true
        project nullable: false
        releaseDate nullable: true
        status blank: false, nullable: false, inList: ["ToDo", "Planning", "In Progress", "Released"]
        testCycles nullable: true
    }
}
