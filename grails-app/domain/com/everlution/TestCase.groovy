package com.everlution

class TestCase {

    String description
    String name
    static hasMany = [steps: TestStep]

    static constraints = {
        description blank: true, nullable: false, maxSize: 1000
        name blank: false, maxSize: 255, nullable: false
        steps nullable: true
    }
}