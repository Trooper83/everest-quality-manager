package com.everlution.test.testresult

import com.everlution.AutomatedTest
import com.everlution.Project
import com.everlution.TestResult
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestResultSpec extends Specification implements DomainUnitTest<TestResult> {

    @Shared int id

    void "instances are persisted"() {
        def project = new Project(name: "tc domain project321", code: "td5").save()
        def at = new AutomatedTest(fullName: "First test", project: project).save()
        new TestResult(automatedTest: at, result: 'Passed').save()
        new TestResult(automatedTest: at, result: 'Passed').save()

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

    void "automatedTest cannot be null"() {
        when:
        domain.automatedTest = null

        then:
        !domain.validate(["automatedTest"])
        domain.errors["automatedTest"].code == "nullable"
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
