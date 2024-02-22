package com.everlution

class TestResult {

    AutomatedTest automatedTest
    String failureCause
    Date dateCreated
    String result
    TestRun testRun

    static mapping = {
        automatedTest cascade: "none"
        testRun cascade: "none"
    }

    static constraints = {
        automatedTest nullable: false
        failureCause nullable: true, blank: true, maxSize: 500
        result blank: false, nullable: false, inList: ["Failed", "Passed", "Skipped"]
        testRun nullable: false
    }
}
