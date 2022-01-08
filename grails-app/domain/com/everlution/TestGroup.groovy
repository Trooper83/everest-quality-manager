package com.everlution

class TestGroup {

    Date dateCreated
    String name
    Project project
    Collection testCases

    static belongsTo = [ Project ]

    static hasMany = [ testCases: TestCase ]

    static mapping = {
        testCases cascade: "none"
    }

    static constraints = {
        name blank: false, maxSize: 255, nullable: false
        project nullable: false
        testCases nullable: true
    }
}
