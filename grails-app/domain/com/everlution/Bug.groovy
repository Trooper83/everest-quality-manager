package com.everlution

class Bug {

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
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        name blank: false, maxSize: 255, nullable: false
        project nullable: false
    }
}
