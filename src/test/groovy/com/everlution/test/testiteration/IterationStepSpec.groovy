package com.everlution.test.testiteration

import com.everlution.domains.IterationStep
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class IterationStepSpec extends Specification implements DomainUnitTest<IterationStep> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        new IterationStep(act: "First Test step", result: "test").save()

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
        domain.act = "Test case name"

        then:
        domain.act == "Test case name"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.act == null
        System.identityHashCode(domain) != id
    }

    void "act can be null when result is not"() {
        when:
        domain.act = null
        domain.result = "test"

        then:
        domain.validate(["act"])
    }

    void "test act can be blank"() {
        when:
        domain.act = ""

        then:
        domain.validate(["act"])
    }

    void "validation fails if act and result are null"() {
        when:
        domain.act = null
        domain.result = null

        then:
        !domain.validate(["act"])
        domain.errors["act"].code == "validator.invalid"
    }

    void "test act cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.act = str

        then: "act validation fails"
        !domain.validate(["act"])
        domain.errors["act"].code == "maxSize.exceeded"
    }

    void "test act validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.act = str

        then: "act validation passes"
        domain.validate(["act"])
    }

    void "validation fails if result and act are null"() {
        when:
        domain.result = null
        domain.act = null

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "validator.invalid"
    }

    void "result can be null when act is not"() {
        when:
        domain.result = null
        domain.act = "test"

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

    void "data can be null"() {
        when:
        domain.data = null

        then:
        domain.validate(["data"])
    }

    void "data can be blank"() {
        when:
        domain.data = ""

        then:
        domain.validate(["data"])
    }

    void "data cannot exceed 500 characters"() {
        when: "for a string of 256 characters"
        String str = "a" * 501
        domain.data = str

        then: "validation fails"
        !domain.validate(["data"])
        domain.errors["data"].code == "maxSize.exceeded"
    }

    void "data validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.data = str

        then: "validation passes"
        domain.validate(["data"])
    }
}
