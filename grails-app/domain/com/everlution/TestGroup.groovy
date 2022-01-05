package com.everlution

class TestGroup {

    String name
    Collection testCases

    static belongsTo = [ Project ]

    static hasMany = [ testCases: TestCase ]

    static mapping = {
        testCases cascade: "none"
    }

    static constraints = {
        name blank: false, maxSize: 255, nullable: false
        testCases nullable: true
    }
}
