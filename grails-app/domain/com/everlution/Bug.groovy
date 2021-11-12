package com.everlution

class Bug {

    Area area
    String creator
    Date dateCreated
    String description
    List environments
    String name
    Project project
    List steps

    static hasMany = [environments: Environment, steps: Step]

    static mapping = {
        environments cascade: 'none'
        project cascade: 'none'
        steps cascade: 'all-delete-orphan'
    }

    static constraints = {
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
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        name blank: false, maxSize: 255, nullable: false
        project nullable: false
        environments nullable: true, validator: { val, Bug obj ->
            if(val == null) {
                return
            }
            if(obj.project == null || obj.project.environments == null) {
                return false
            }
            def ids = obj.project.environments*.id
            def c = val.collect { it.id }
            ids.containsAll(c)
        }
    }
}
