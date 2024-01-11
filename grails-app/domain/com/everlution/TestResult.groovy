package com.everlution

class TestResult {

    Date dateCreated
    String result
    TestCase testCase

    static mapping = {
        testCase cascade: "none"
    }

    static constraints = {
        result blank: false, nullable: false, inList: ["Failed", "Passed", "Skipped"]
        testCase nullable: false
    }
}
