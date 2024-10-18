package com.manager.quality.everest.test.unit.security

import com.manager.quality.everest.domains.Person
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PersonDomainSpec extends Specification implements DomainUnitTest<Person> {

    void "test instances are persisted"() {
        setup: "save two instances"
        new Person(email: "email1@email.com", password: "!Testing25").save()
        new Person(email: "email12@email.com", password: "!Testing25").save()

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

    void "password validation fails"(String password) {
        when:
        domain.password = password

        then: "validation fails"
        !domain.validate(["password"])
        domain.errors["password"].code == "matches.invalid"

        where:
        password << ["IFailWithoutNumber!", "IFailWithoutSpecChar111", "ifailwithoutuppercase#2022", "IFAILWITHOUTLOWER#200"]
    }

    void "password validates with uppercase lowercase spec char and number"() {
        when:
        domain.password = "!Ishouldvalidate#2000"

        then:
        domain.validate(["password"])
    }

    void "password validates with 8 characters"() {
        when:
        String str = "aA#2022!"
        domain.password = str

        then: "validation passes"
        str.length() == 8
        domain.validate(["password"])
    }

    void "password validates with 256 characters"() {
        when:
        String str = "aA#2022!" * 32
        domain.password = str

        then: "validation passes"
        str.length() == 256
        domain.validate(["password"])
    }

    void "password fails validation with 7 characters"() {
        when:
        String str = "aA#2022"
        domain.password = str

        then: "validation fails"
        str.length() == 7
        !domain.validate(["password"])
        domain.errors["password"].code == "size.toosmall"
    }

    void "password fails validation with 257 characters"() {
        when:
        String str = "aA#2022!" * 32
        domain.password = str.concat("a")

        then: "validation fails"
        domain.password.length() == 257
        !domain.validate(["password"])
        domain.errors["password"].code == "size.toobig"
    }

    void "email must be unique"() {
        given: "create person instance"
        new Person(email: "test@test.com", password: "!Testing25").save()

        when: "create a person with same email"
        def failed = new Person(email: "test@test.com", password: "!Testing25")

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