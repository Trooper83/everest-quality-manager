package com.everlution

class TestCase {

    String creator
    Date dateCreated
    String description
    String executionMethod
    String name
    List steps
    static hasMany = [steps: TestStep]
    String type

    static constraints = {
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        executionMethod blank: false, nullable: false, inList: ["Automated", "Manual"]
        name blank: false, maxSize: 255, nullable: false
        steps nullable: true
        type blank: false, nullable: false, inList: ["API", "UI"]
    }
}