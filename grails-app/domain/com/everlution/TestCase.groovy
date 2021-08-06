package com.everlution

class TestCase {

    Date dateCreated
    String description
    String name
    static hasMany = [steps: TestStep]

    static constraints = {
        description blank: true, nullable: true, maxSize: 1000
        name blank: false, maxSize: 255, nullable: false
        steps nullable: true
    }
}