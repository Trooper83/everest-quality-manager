package com.everlution.test.testgroup

import com.everlution.Person
import com.everlution.Project
import com.everlution.TestCase
import com.everlution.TestGroup
import grails.test.hibernate.HibernateSpec
import grails.testing.gorm.DataTest

class TestGroupHibernateSpec extends HibernateSpec implements DataTest {

    void "delete does not cascade to test case"() {
        given: "test case with group"
        mockDomain(Project)
        Person person = new Person(email: "test@test.com", password: "password").save()
        TestGroup group = new TestGroup(name: "group")
        Project project = new Project(name: "Test Case Project for groups", code: "TCF", testGroups: [group]).save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        group.addToTestCases(testCase)

        expect:
        group.id != null
        TestCase.findById(testCase.id) != null

        when:
        group.delete(flush: true)

        then:
        TestGroup.findById(group.id) == null
        TestCase.findById(testCase.id) != null
    }
}
