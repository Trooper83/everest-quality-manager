package com.everlution

class Bug {

    String creator
    Date dateCreated
    String description
    String name
    Project project

    static belongsTo = Project

    static mapping = {
        project cascade: 'none'
    }

    static constraints = {
        creator blank: false, nullable: false, maxSize: 100
        description blank: true, nullable: true, maxSize: 1000
        name blank: false, maxSize: 255, nullable: false
        project nullable: false
    }
}
