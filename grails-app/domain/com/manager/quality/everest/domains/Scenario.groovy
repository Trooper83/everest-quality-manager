package com.manager.quality.everest.domains

class Scenario {

    Area area
    Date dateCreated
    String description
    List environments
    String executionMethod
    String gherkin
    Date lastUpdated
    String name
    Person person
    Platform platform
    Project project
    String type

    static hasMany = [environments: Environment]

    static mapping = {
        area cascade: "none"
        environments cascade: "none"
        person cascade: "none"
        platform cascade: "none"
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
        description blank: true, nullable: true, maxSize: 1000
        executionMethod blank: true, nullable: true, inList: ["Automated", "Manual"]
        gherkin blank: true, nullable: true, maxSize: 2500
        name blank: false, maxSize: 255, nullable: false
        person nullable: false
        platform nullable: true, validator: { val, Scenario obj ->
            if(val == null) {
                return
            }
            if(obj.project == null || obj.project.platforms == null) {
                return false
            }
            def ids = obj.project.platforms*.id
            val.id in ids
        }
        project nullable: false
        type blank: true, nullable: true, inList: ["UI", "API"]
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
