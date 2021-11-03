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
        area nullable: true //TODO: custom validator to ensure area is in project
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        name blank: false, maxSize: 255, nullable: false
        project nullable: false
    }
}
