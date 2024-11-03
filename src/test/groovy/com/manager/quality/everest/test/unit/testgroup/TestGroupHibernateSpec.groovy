package com.manager.quality.everest.test.unit.testgroup

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.domains.TestGroup
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Shared

class TestGroupHibernateSpec extends HibernateSpec {

    @Shared Project project

    def setup() {
        project = new Project(name: "Test Group Project", code: "TGP").save()
    }

    void "test date created auto generated"() {
        when:
        def group = new TestGroup(name: "name", project: project).save()

        then:
        group.dateCreated != null
    }

    void "delete group with test cases throws exception"() {
        given: "test group with case"
        def person = new Person(email: "test@test.com", password: "!Password2022").save()
        Project project = new Project(name: "Test Case Project for groups", code: "TCF").save()
        TestGroup group = new TestGroup(name: "group", project: project).save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        group.addToTestCases(testCase)

        expect:
        TestGroup.findById(group.id) != null
        TestCase.findById(testCase.id) != null

        when:
        group.delete(flush: true)

        then:
        thrown(DataIntegrityViolationException)
    }

    void "removeFrom does not cascade to test case"() {
        given: "test group with case"
        def person = new Person(email: "test@test.com", password: "!Password2022").save()
        Project project = new Project(name: "Test Case Project for groups", code: "TCF").save()
        TestGroup group = new TestGroup(name: "group", project: project).save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        group.addToTestCases(testCase)

        expect:
        TestGroup.findById(group.id) != null
        TestCase.findById(testCase.id) != null

        when:
        group.removeFromTestCases(testCase)

        then:
        TestCase.findById(testCase.id) != null
    }
}
