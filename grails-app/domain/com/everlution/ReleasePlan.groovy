package com.everlution

class ReleasePlan {

    Date dateCreated
    Date lastUpdated
    String name
    String notes
    Date plannedDate
    Person person
    Project project
    Date releaseDate
    String status
    Collection testCycles

    static hasMany = [ testCycles: TestCycle ]

    static mapping = {
        person cascade: "none"
        project cascade: "none"
        testCycles cascade: "all-delete-orphan"
    }

    static constraints = {
        name nullable: false, blank: false, maxSize: 500
        notes nullable: true, blank: true, maxSize: 1000
        person nullable: false
        plannedDate nullable: true
        project nullable: false
        releaseDate nullable: true
        status blank: false, nullable: false, inList: ["Canceled", "ToDo", "Planning", "In Progress", "Released"]
        testCycles nullable: true
    }
}
