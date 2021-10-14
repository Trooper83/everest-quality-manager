package com.everlution

class Project {

    String name
    String code

    static hasMany = [areas: Area, bugs: Bug, testCases: TestCase]

    static mapping = {
        areas fetch: "join"
    }

    static constraints = {
        name blank: false, nullable: false, maxSize: 100, unique: true
        code blank: false, nullable: false, minSize: 3, maxSize: 3, unique: true
    }
}
