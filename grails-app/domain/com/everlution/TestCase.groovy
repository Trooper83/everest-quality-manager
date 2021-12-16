package com.everlution

class TestCase {

    Area area
    Date dateCreated
    String description
    List environments
    String executionMethod
    String name
    Person person
    Project project
    List steps
    String type

    static hasMany = [environments: Environment, steps: Step]

    static mapping = {
        area cascade: "none"
        environments cascade: "none"
        person cascade: "none"
        project cascade: "none"
    }

    static constraints = {
        area nullable: true, validator: { val, TestCase obj ->
            if(val == null) {
                return
            }
            if(obj.project == null || obj.project.areas == null) {
                return false
            }
            def ids = obj.project.areas*.id
            val.id in ids
        }
        description blank: true, nullable: true, maxSize: 1000
        executionMethod blank: false, nullable: false, inList: ["Automated", "Manual"]
        name blank: false, maxSize: 255, nullable: false
        person nullable: false
        project nullable: false
        steps nullable: true
        type blank: false, nullable: false, inList: ["UI", "API"]
        environments nullable: true, validator: { val, TestCase obj ->
            if(val == null) {
                return
            }
            if(obj.project == null || obj.project.environments == null) {
                return false
            }
            def ids = obj.project.environments*.id
            def envIds = val.collect { it.id }
            ids.containsAll(envIds)
        }
    }
}