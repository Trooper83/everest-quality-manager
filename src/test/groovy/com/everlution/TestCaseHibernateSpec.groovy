package com.everlution

import grails.test.hibernate.HibernateSpec

class TestCaseHibernateSpec extends HibernateSpec {

    void "test date created auto generated"() {
        when:
        Project project = new Project(name: "Test Case Date Project", code: "TCD")
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        then:
        testCase.dateCreated != null
    }
}
