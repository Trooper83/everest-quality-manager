package com.manager.quality.everest.test.unit.environment

import com.manager.quality.everest.domains.Environment
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class EnvironmentSpec extends Specification implements DomainUnitTest<Environment> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        new Environment(name: "env of the application").save()

        expect:
        Environment.count() == 1
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.name = "env name"

        then:
        domain.name == "env name"
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

    void "test name cannot exceed 100 characters"() {
        when: "for a string of 101 characters"
        String str = "a" * 101
        domain.name = str

        then: "name validation fails"
        !domain.validate(["name"])
        domain.errors["name"].code == "maxSize.exceeded"
    }

    void "test name validates with 100 characters"() {
        when: "for a string of 100 characters"
        String str = "a" * 100
        domain.name = str

        then: "name validation passes"
        domain.validate(["name"])
    }
}
