package com.everlution

class TestCase {

    String creator
    Date dateCreated
    String description
    String executionMethod
    String name
    Project project
    List steps
    String type

    static belongsTo = Project
    static mappedBy = [project: "none"]
    static hasMany = [steps: TestStep]

    static constraints = {
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        executionMethod blank: false, nullable: false, inList: ["Automated", "Manual"]
        name blank: false, maxSize: 255, nullable: false
        steps nullable: true
        type blank: false, nullable: false, inList: ["UI", "API"]
    }
}