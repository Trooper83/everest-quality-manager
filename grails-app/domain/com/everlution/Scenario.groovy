package com.everlution

class Scenario {

    Area area
    String creator
    Date dateCreated
    String description
    List environments
    String executionMethod
    String gherkin
    String name
    Project project
    String type

    static hasMany = [environments: Environment]

    static mapping = {
        area cascade: "none"
        environments cascade: "none"
        project cascade: "none"
    }

    static constraints = {
        area nullable: true, validator: { val, Scenario obj ->
            if(val == null) {
                return
            }
            if(obj.project == null || obj.project.areas == null) {
                return false
            }
            def ids = obj.project.areas*.id
            val.id in ids
        }
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        executionMethod blank: false, nullable: false, inList: ["Automated", "Manual"]
        gherkin blank: true, nullable: true, maxSize: 2500
        name blank: false, maxSize: 255, nullable: false
        project nullable: false
        type blank: false, nullable: false, inList: ["UI", "API"]
        environments nullable: true, validator: { val, Scenario obj ->
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