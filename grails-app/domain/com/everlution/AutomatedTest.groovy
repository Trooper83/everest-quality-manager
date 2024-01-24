package com.everlution

class AutomatedTest {

    Date dateCreated
    String fullName
    String name
    Project project

    static hasMany = [testResults: TestResult]

    static mapping = {
        testResults cascade: "none"
        project cascade: "none"
    }

    static constraints = {
        fullName nullable: false, blank: false, maxSize: 500, unique: 'project'
        name nullable: true, blank: true, maxSize: 255
        project nullable: false
    }
}
