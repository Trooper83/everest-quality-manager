package com.everlution

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class BugSpec extends Specification implements DomainUnitTest<Bug> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        def project = new Project(name: "tc domain project321", code: "td5").save()
        new Bug(creator: "test", name: "First Bug", description: "test", project: project).save()
        new Bug(creator: "test",name: "Second Bug", description: "test", project: project).save()

        expect:
        Bug.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.name = "bug name"

        then:
        domain.name == "bug name"
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

    void "area can be null"() {
        when:
        domain.area = null

        then:
        domain.validate(["area"])
    }
}
