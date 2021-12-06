package com.everlution.test.security

import com.everlution.Person
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PersonDomainSpec extends Specification implements DomainUnitTest<Person> {

    void "test instances are persisted"() {
        setup: "save two instances"
        new Person(email: "email1@email.com", password: "CHI").save()
        new Person(email: "email1@email.com", password: "SAN").save()

        expect: "count is 2"
        Person.count() == 2
    }

    void "test domain instance"() {
        when: "set domain name"
        domain.email = "email@email.com"

        then: "domain name equals the previous set value"
        domain.email == "email@email.com"
    }

    void "email validates with email format"() {
        when:
        domain.email = "email1@email.com"

        then:
        domain.validate(["email"])
    }

    void "email fails validation with invalid email"(String email) {
        when: "property"
        domain.email = email

        then: "validation fails"
        !domain.validate(["email"])
        domain.errors["email"].code == "email.invalid"

        where:
        email << ["test", "test@", "test@test", "@test", "@test.com", " @test.com", "test.com", "@.com", "test@.com"]
    }

    void "email cannot be null"() {
        when: "property null"
        domain.email = null

        then: "validation fails"
        !domain.validate(["email"])
        domain.errors["email"].code == "nullable"
    }

    void "email cannot be blank"() {
        when: "property blank"
        domain.email = ""

        then: "validation fails"
        !domain.validate(["email"])
        domain.errors["email"].code == "blank"
    }

    void "password cannot be null"() {
        when: "property null"
        domain.password = null

        then: "validation fails"
        !domain.validate(["password"])
        domain.errors["password"].code == "nullable"
    }

    void "password cannot be blank"() {
        when: "property blank"
        domain.password = ""

        then: "validation fails"
        !domain.validate(["password"])
        domain.errors["password"].code == "blank"
    }

    void "person username must be unique"() {
        given: "create person instance"
        new Person(email: "test@test.com", password: "CHI").save()

        when: "create a person with same email"
        def failed = new Person(email: "test@test.com", password: "CHI")

        then: "email fails validation"
        !failed.validate(["email"])

        and: "unique error"
        failed.errors["email"].code == "unique"

        and: "save fails"
        !failed.save()
        Person.count() == old(Person.count())
    }

    void "enabled defaults true"() {
        expect:
        domain.enabled
    }

    void "accountExpired defaults false"() {
        expect:
        !domain.accountExpired
    }

    void "accountLocked defaults false"() {
        expect:
        !domain.accountLocked
    }

    void "passwordExpired defaults false"() {
        expect:
        !domain.passwordExpired
    }
}