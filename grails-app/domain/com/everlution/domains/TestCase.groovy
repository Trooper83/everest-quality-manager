package com.everlution.domains

class TestCase {

    Area area
    Date dateCreated
    String description
    Collection environments
    String executionMethod
    Date lastUpdated
    String name
    Person person
    Platform platform
    Project project
    List steps
    List testGroups
    String type
    String verify

    static hasMany = [ environments: Environment, steps: Step, testGroups: TestGroup ]

    static mapping = {
        area cascade: "none"
        environments cascade: "none"
        person cascade: "none"
        project cascade: "none"
        steps cascade: "all-delete-orphan"
        testGroups cascade: "none"
    }

    static constraints = {
        area nullable: true, validator: { val, TestCase obj ->
            if(val == null) {
                return true
            }
            if(obj.project == null || obj.project.areas == null) {
                return false
            }
            def ids = obj.project.areas*.id
            val.id in ids
        }
        description blank: true, nullable: true, maxSize: 1000
        executionMethod blank: true, nullable: true, inList: ["Automated", "Manual"]
        name blank: false, maxSize: 255, nullable: false
        person nullable: false
        platform nullable: true, validator: { val, TestCase obj ->
            if(val == null) {
                return true
            }
            if(obj.project == null || obj.project.platforms == null) {
                return false
            }
            def ids = obj.project.platforms*.id
            val.id in ids
        }
        project nullable: false
        steps nullable: true
        type blank: true, nullable: true, inList: ["UI", "API"]
        environments validator: { val, TestCase obj ->
            if(val == null) {
                return
            }
            if(obj.project == null || obj.project.environments == null) {
                return false
            }
            def ids = obj.project.environments*.id
            def envIds = val.collect { it.id }
            ids.containsAll(envIds)
        }
        testGroups validator: { val, TestCase obj ->
            if(val == null) {
                return
            }
            if(obj.project == null || obj.project.testGroups == null) {
                return false
            }
            def ids = obj.project.testGroups*.id
            def groupIds = val.collect { it.id }
            ids.containsAll(groupIds)
        }
        verify nullable: true, blank:true, maxSize: 500
    }
}