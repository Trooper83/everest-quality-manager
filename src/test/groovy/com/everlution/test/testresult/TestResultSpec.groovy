package com.everlution.test.testresult

import com.everlution.Person
import com.everlution.Project
import com.everlution.TestCase
import com.everlution.TestResult
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestResultSpec extends Specification implements DomainUnitTest<TestResult> {

    @Shared int id

    void "instances are persisted"() {
        def project = new Project(name: "tc domain project321", code: "td5").save()
        def person = new Person(email: "test@test.com", password: "test")
        def tc = new TestCase(person: person, name: "First test", description: "test", project: project).save()
        new TestResult(testCase: tc, result: 'Passed').save()
        new TestResult(testCase: tc, result: 'Failed').save()

        expect:
        TestResult.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.result = "result"

        then:
        domain.result == "result"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.result == null
        System.identityHashCode(domain) != id
    }

    void "testCase cannot be null"() {
        when:
        domain.testCase = null

        then:
        !domain.validate(["testCase"])
        domain.errors["testCase"].code == "nullable"
    }

    void "result cannot be null"() {
        when:
        domain.result = null

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "nullable"
    }

    void "result cannot be blank"() {
        when:
        domain.result = ""

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "blank"
    }

    void "result value in list"(String value) {
        when:
        domain.result = value

        then:
        domain.validate(["result"])

        where:
        value << ["Passed", "Failed", "Skipped"]
    }

    void "result value not in list"() {
        when:
        domain.result = "test"

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "not.inList"
    }
}
