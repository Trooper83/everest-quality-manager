package com.manager.quality.everest.domains

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
        failureCause nullable: true, blank: true, maxSize: 2500
        result blank: false, nullable: false, inList: ["FAILED", "PASSED", "SKIPPED"]
        testRun nullable: false
    }
}
