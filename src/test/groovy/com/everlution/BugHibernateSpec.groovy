package com.everlution

import grails.test.hibernate.HibernateSpec

class BugHibernateSpec extends HibernateSpec {

    void "date created populates on save"() {
        given: "a bug instance"
        def project = new Project(name: "tc domain project", code: "tdp")
        def testCase = new TestCase(creator: "test", name: "First Test Case in bugs", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def bug = new Bug(creator: "test", name: "First Bug", description: "test", testCases: [testCase]).save()

        expect: "bug has date created"
        bug.dateCreated != null
    }
}
