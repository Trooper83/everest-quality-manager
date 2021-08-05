package com.everlution

class TestStep {

    String action
    String result

    static belongsTo = TestCase
    static hasMany = [testCase: TestCase]

    static constraints = {
        action blank: true, maxSize: 500, nullable: false
        result blank: true, maxSize: 500, nullable: false
    }
}
