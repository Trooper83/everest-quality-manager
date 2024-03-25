package com.everlution.domains

class AutomatedTest {

    Date dateCreated
    String fullName
    String name
    Project project

    static mapping = {
        project cascade: "none"
    }

    static constraints = {
        fullName nullable: false, blank: false, maxSize: 500, unique: 'project'
        name nullable: true, blank: true, maxSize: 255
        project nullable: false
    }
}
