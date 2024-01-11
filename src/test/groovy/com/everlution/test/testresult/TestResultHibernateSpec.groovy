package com.everlution.test.testresult

import com.everlution.Person
import com.everlution.Project
import com.everlution.TestCase
import com.everlution.TestResult
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class TestResultHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Project, Person, TestResult] }

    @Shared Person person
    @Shared Project project

    def setup() {
        project = new Project(name: "tc domain project321", code: "td5").save()
        person = new Person(email: "test@test.com", password: "!Testing@t123").save()
    }

    void "dateCreated populated on save"() {
        when:
        def tc = new TestCase(person: person, name: "First test", description: "test", project: project).save()
        TestResult r = new TestResult(testCase: tc, result: 'Passed').save()

        then:
        r.dateCreated != null
    }

    void "save does not cascade to test case"() {
        when:
        def tc = new TestCase(person: person, name: "First test", description: "test", project: project)
        new TestResult(testCase: tc, result: 'Passed').save()

        then:
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete does not cascade to test case"() {
        given:
        def tc = new TestCase(person: person, name: "First test", description: "test", project: project).save()
        def r = new TestResult(testCase: tc, result: 'Passed').save()

        expect:
        TestCase.findById(tc.id) != null

        when:
        r.delete(flush: true)

        then:
        TestCase.findById(tc.id) != null
    }
}
