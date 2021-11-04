package com.everlution

class TestCase {

    Area area
    String creator
    Date dateCreated
    String description
    String executionMethod
    String name
    Project project
    List steps
    String type

    static hasMany = [steps: Step]

    static mapping = {
        project cascade: "none"
        steps cascade: "all-delete-orphan"
    }

    static constraints = {
        area nullable: true, validator: { val, TestCase obj ->
            if(val == null) {
                return true
            }
            val in obj.project?.areas
        }
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        executionMethod blank: false, nullable: false, inList: ["Automated", "Manual"]
        name blank: false, maxSize: 255, nullable: false
        project nullable: false
        steps nullable: true
        type blank: false, nullable: false, inList: ["UI", "API"]
    }
}