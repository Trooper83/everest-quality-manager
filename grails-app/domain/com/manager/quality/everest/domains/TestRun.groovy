package com.manager.quality.everest.domains

class TestRun {

    Date dateCreated
    String name
    Project project
    Collection testResults

    static hasMany = [testResults: TestResult]

    static mapping = {
        project cascade: "none"
        testResults cascade: "all-delete-orphan"
    }

    static constraints = {
        name nullable: false, blank: false, maxSize: 255
        project nullable: false
        testResults nullable: true
    }
}
