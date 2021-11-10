package com.everlution

class Bug {

    Area area
    String creator
    Date dateCreated
    String description
    String name
    Project project
    List steps

    static hasMany = [steps: Step]

    static mapping = {
        project cascade: 'none'
        steps cascade: 'all-delete-orphan'
    }

    static constraints = {
        area nullable: true, validator: { val, Bug obj ->
            if(val == null) {
                return true
            }
            val in obj.project.areas
        }
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        name blank: false, maxSize: 255, nullable: false
        project nullable: false
    }
}
