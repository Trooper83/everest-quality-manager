package com.everlution

class TestResult {

    AutomatedTest automatedTest
    Date dateCreated
    String result

    static mapping = {
        automatedTest cascade: "none"
    }

    static constraints = {
        automatedTest nullable: false
        result blank: false, nullable: false, inList: ["Failed", "Passed", "Skipped"]
    }
}
