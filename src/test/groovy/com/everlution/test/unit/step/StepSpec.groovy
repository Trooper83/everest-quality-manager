package com.everlution.test.unit.step

import com.everlution.domains.Step
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class StepSpec extends Specification implements DomainUnitTest<Step> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        new Step(act: "First Test step", result: "test").save()

        expect:
        Step.count() == 1
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

    void "action can be null when result is not"() {
        when:
        domain.act = null
        domain.result = "test"

        then:
        domain.validate(["action"])
    }

    void "test action can be blank"() {
        when:
        domain.act = ""

        then:
        domain.validate(["action"])
    }

    void "validation fails if action and result are null"() {
        when:
        domain.act = null
        domain.result = null

        then:
        !domain.validate(["act"])
        domain.errors["act"].code == "validator.invalid"
    }

    void "test action cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.act = str

        then: "action validation fails"
        !domain.validate(["act"])
        domain.errors["act"].code == "maxSize.exceeded"
    }

    void "test action validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.act = str

        then: "action validation passes"
        domain.validate(["act"])
    }

    void "validation fails if result and action are null"() {
        when:
        domain.result = null
        domain.act = null

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "validator.invalid"
    }

    void "result can be null when action is not"() {
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

    void "isBuilderStep defaults to false"() {
        expect:
        !domain.isBuilderStep
    }

    void "dateCreated can be null"() {
        when:
        domain.dateCreated = null

        then:
        domain.validate(["dateCreated"])
    }

    void "lastUpdated can be null"() {
        when:
        domain.lastUpdated = null

        then:
        domain.validate(["lastUpdated"])
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

    void "template can be null"() {
        when:
        domain.template = null

        then:
        domain.validate(["template"])
    }
}
