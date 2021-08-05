package com.everlution

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestStepSpec extends Specification implements DomainUnitTest<TestStep> {

    @Shared int id

    def setup() {
    }

    def cleanup() {
    }

    void "test instances are persisted"() {
        setup:
        new TestStep(action: "First Test step", result: "test").save()

        expect:
        TestStep.count() == 1
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.action = "Test case name"

        then:
        domain.action == "Test case name"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.action == null
        System.identityHashCode(domain) != id
    }

    void "test action cannot be null"() {
        when:
        domain.action = null

        then:
        !domain.validate(["action"])
        domain.errors["action"].code == "nullable"
    }

    void "test action can be blank"() {
        when:
        domain.action = ""

        then:
        domain.validate(["action"])
    }

    void "test action cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.action = str

        then: "action validation fails"
        !domain.validate(["action"])
        domain.errors["action"].code == "maxSize.exceeded"
    }

    void "test action validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.action = str

        then: "action validation passes"
        domain.validate(["action"])
    }

    void "test result cannot be null"() {
        when:
        domain.result = null

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "nullable"
    }

    void "test result can be blank"() {
        when:
        domain.result = ""

        then:
        domain.validate(["result"])
    }

    void "test result cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.result = str

        then: "result validation fails"
        !domain.validate(["result"])
        domain.errors["result"].code == "maxSize.exceeded"
    }

    void "test result validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.result = str

        then: "result validation passes"
        domain.validate(["result"])
    }

    void "test case cannot be null"() {
        when:
        def d = domain
        domain.testCase = null

        then:
        domain.validate(["testCase"])
    }
}
