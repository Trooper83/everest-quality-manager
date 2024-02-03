package com.everlution

class TestResult {

    AutomatedTest automatedTest
    String failureCause
    Date dateCreated
    String result

    static mapping = {
        automatedTest cascade: "none"
    }

    static constraints = {
        automatedTest nullable: false
        failureCause nullable: true, blank: true, maxSize: 500
        result blank: false, nullable: false, inList: ["Failed", "Passed", "Skipped"]
    }
}
