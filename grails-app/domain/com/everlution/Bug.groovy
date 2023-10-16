package com.everlution

class Bug {

    String actual
    Area area
    Date dateCreated
    String description
    List environments
    String expected
    Date lastUpdated
    String name
    Person person
    String platform
    Project project
    String status
    List steps

    static hasMany = [environments: Environment, steps: Step]

    static mapping = {
        area cascade: "none"
        environments cascade: "none"
        person cascade: "none"
        project cascade: "none"
        steps cascade: "save-update"
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
        person nullable: false
        platform blank: true, nullable: true, inList: ["Android", "iOS", "Web"]
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
        status blank: false, nullable: false, inList: ["Open", "Closed"]
        steps nullable: true
    }
}
