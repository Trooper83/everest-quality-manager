package com.manager.quality.everest.domains

class Bug {

    String actual
    Area area
    Date dateCreated
    String description
    List environments
    String expected
    Date lastUpdated
    String name
    String notes
    Person person
    Platform platform
    Project project
    String status
    List steps

    static hasMany = [environments: Environment, steps: Step]

    static mapping = {
        area cascade: "none"
        environments cascade: "none"
        person cascade: "none"
        project cascade: "none"
        steps cascade: "all-delete-orphan"
    }

    static constraints = {
        actual nullable: true, blank: true, maxSize: 500
        area nullable: true, validator: { val, Bug obj ->
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
        expected nullable: true, blank: true, maxSize: 500
        name blank: false, maxSize: 255, nullable: false
        notes nullable: true, blank: true, maxSize: 500
        person nullable: false
        platform nullable: true, validator: { val, Bug obj ->
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
        environments nullable: true, validator: { val, Bug obj ->
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
        status blank: false, nullable: false, inList: ["Open", "Fixed", "Closed"]
        steps nullable: true
    }
}
