package com.manager.quality.everest.test.unit.testiteration

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.TestIterationResult
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestIterationResultSpec extends Specification implements DomainUnitTest<TestIterationResult> {

    @Shared int id

    void "instances are persisted"() {
        setup:
        def person = new Person(email: "test@test.com", password: "test")
        new TestIterationResult(person: person, result: "FAILED").save()

        expect:
        TestIterationResult.count() == 1
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.notes = "test notes"

        then:
        domain.notes == "test notes"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.notes == null
        System.identityHashCode(domain) != id
    }

    void "notes can be null"() {
        when:
        domain.notes = null

        then:
        domain.validate(["notes"])
    }

    void "notes can be blank"() {
        when:
        domain.notes = ""

        then:
        domain.validate(["notes"])
    }

    void "notes cannot exceed 1000 characters"() {
        when: "for a string of 1001 characters"
        String str = "a" * 1001
        domain.notes = str

        then: "validation fails"
        !domain.validate(["notes"])
        domain.errors["notes"].code == "maxSize.exceeded"
    }

    void "notes validates with 1000 characters"() {
        when: "for a string of 1000 characters"
        String str = "a" * 1000
        domain.notes = str

        then: "validation passes"
        domain.validate(["notes"])
    }

    void "person cannot be null"() {
        when:
        domain.person = null

        then:
        !domain.validate(["person"])
        domain.errors["person"].code == "nullable"
    }

    void "dateCreated can be null"() {
        when:
        domain.dateCreated = null

        then:
        domain.validate(["dateCreated"])
    }

    void "result cannot be blank"() {
        when:
        domain.result = ""

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "blank"
    }

    void "result cannot be null"() {
        when:
        domain.result = null

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "nullable"
    }

    void "result validates with value in list"(String value) {
        when:
        domain.result = value

        then:
        domain.validate(["result"])

        where:
        value << ["SKIPPED", "PASSED", "FAILED"]
    }

    void "result fails validation with value not in list"() {
        when:
        domain.result = "test"

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "not.inList"
    }
}
