package com.everlution

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestCaseSpec extends Specification implements DomainUnitTest<TestCase> {

    @Shared int id

    def setup() {
    }

    def cleanup() {
    }

    void "test instances are persisted"() {
        setup:
        new TestCase(creator: "test", name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI").save()
        new TestCase(creator: "test",name: "Second Test Case", description: "test",
                executionMethod: "Automated", type: "API").save()

        expect:
        TestCase.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.name = "Test case name"

        then:
        domain.name == "Test case name"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.name == null
        System.identityHashCode(domain) != id
    }

    void "test name cannot be null"() {
        when:
        domain.name = null

        then:
        !domain.validate(["name"])
        domain.errors["name"].code == "nullable"
    }

    void "test name cannot be blank"() {
        when:
        domain.name = ""

        then:
        !domain.validate(["name"])
        domain.errors["name"].code == "blank"
    }

    void "test name cannot exceed 255 characters"() {
        when: "for a string of 256 characters"
        String str = "a" * 256
        domain.name = str

        then: "name validation fails"
        !domain.validate(["name"])
        domain.errors["name"].code == "maxSize.exceeded"
    }

    void "test name validates with 255 characters"() {
        when: "for a string of 255 characters"
        String str = "a" * 255
        domain.name = str

        then: "name validation passes"
        domain.validate(["name"])
    }

    void "test description can be null"() {
        when:
        domain.description = null

        then:
        domain.validate(["description"])
    }

    void "test description can be blank"() {
        when:
        domain.description = ""

        then:
        domain.validate(["description"])
    }

    void "test description cannot exceed 1000 characters"() {
        when: "for a string of 1001 characters"
        String str = "a" * 1001
        domain.description = str

        then: "description validation fails"
        !domain.validate(["description"])
        domain.errors["description"].code == "maxSize.exceeded"
    }

    void "test description validates with 1000 characters"() {
        when: "for a string of 1000 characters"
        String str = "a" * 1000
        domain.description = str

        then: "description validation passes"
        domain.validate(["description"])
    }

    void "test steps can be null"() {
        when:
        domain.steps = null

        then:
        domain.validate(["steps"])
    }

    void "test execution method cannot be null"() {
        when:
        domain.executionMethod = null

        then:
        !domain.validate(["executionMethod"])
        domain.errors["executionMethod"].code == "nullable"
    }

    void "test execution method cannot be blank"() {
        when:
        domain.executionMethod = ""

        then:
        !domain.validate(["executionMethod"])
        domain.errors["executionMethod"].code == "blank"
    }

    void "test execution method value in list"(String value) {
        when:
        domain.executionMethod = value

        then:
        domain.validate(["executionMethod"])

        where:
        value       | _
        "Automated" | _
        "Manual"    | _
    }

    void "test execution method value not in list"() {
        when:
        domain.executionMethod = "test"

        then:
        !domain.validate(["executionMethod"])
        domain.errors["executionMethod"].code == "not.inList"
    }

    void "test type cannot be null"() {
        when:
        domain.type = null

        then:
        !domain.validate(["type"])
        domain.errors["type"].code == "nullable"
    }

    void "test type cannot be blank"() {
        when:
        domain.type = ""

        then:
        !domain.validate(["type"])
        domain.errors["type"].code == "blank"
    }

    void "test type value in list"(String value) {
        when:
        domain.type = value

        then:
        domain.validate(["type"])

        where:
        value | _
        "API" | _
        "UI"  | _
    }

    void "test type value not in list"() {
        when:
        domain.type = "test"

        then:
        !domain.validate(["type"])
        domain.errors["type"].code == "not.inList"
    }

    void "test creator cannot be null"() {
        when:
        domain.creator = null

        then:
        !domain.validate(["creator"])
        domain.errors["creator"].code == "nullable"
    }

    void "test creator cannot be blank"() {
        when:
        domain.creator = ""

        then:
        !domain.validate(["creator"])
        domain.errors["creator"].code == "blank"
    }

    void "test creator cannot exceed 100 characters"() {
        when: "for a string of 101 characters"
        String str = "a" * 101
        domain.creator = str

        then: "creator validation fails"
        !domain.validate(["creator"])
        domain.errors["creator"].code == "maxSize.exceeded"
    }

    void "test creator validates with 100 characters"() {
        when: "for a string of 100 characters"
        String str = "a" * 100
        domain.creator = str

        then: "validation passes"
        domain.validate(["creator"])
    }
}
