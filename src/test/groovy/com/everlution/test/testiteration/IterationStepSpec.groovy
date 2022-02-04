package com.everlution.test.testiteration

import com.everlution.IterationStep
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class IterationStepSpec extends Specification implements DomainUnitTest<IterationStep> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        new IterationStep(action: "First Test step", result: "test").save()

        expect:
        IterationStep.count() == 1
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

    void "action can be null when result is not"() {
        when:
        domain.action = null
        domain.result = "test"

        then:
        domain.validate(["action"])
    }

    void "test action can be blank"() {
        when:
        domain.action = ""

        then:
        domain.validate(["action"])
    }

    void "validation fails if action and result are null"() {
        when:
        domain.action = null
        domain.result = null

        then:
        !domain.validate(["action"])
        domain.errors["action"].code == "validator.invalid"
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

    void "validation fails if result and action are null"() {
        when:
        domain.result = null
        domain.action = null

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "validator.invalid"
    }

    void "result can be null when action is not"() {
        when:
        domain.result = null
        domain.action = "test"

        then:
        domain.validate(["result"])
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
}
