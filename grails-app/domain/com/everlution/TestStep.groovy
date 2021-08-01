package com.everlution

class TestStep {

    String action
    String result

    static constraints = {
    }

    static belongsTo = [testCase: TestCase]
    static hasMany = [testCase: TestCase]
}
