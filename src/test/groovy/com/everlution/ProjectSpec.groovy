package com.everlution

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class ProjectSpec extends Specification implements DomainUnitTest<Project> {

    @Shared int id

    void "test instances are persisted"() {
        setup: "save two instances"
        new Project(name: "first project", code: "CHI").save()
        new Project(name: "second project", code: "SAN").save()

        expect: "count is 2"
        Project.count() == 2
    }

    void "test domain instance"() {
        setup: "get the domain's hashcode"
        id = System.identityHashCode(domain)

        expect: "domain is not null"
        domain != null
        domain.hashCode() == id

        when: "set domain name"
        domain.name = "project name"

        then: "domain name equals the previous set value"
        domain.name == "project name"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.name == null
        System.identityHashCode(domain) != id
    }

    void "project name cannot be null"() {
        when: "set domain name to null"
        domain.name = null

        then: "domain fails to validate with nullable error"
        !domain.validate(["name"])
        domain.errors["name"].code == "nullable"
    }

    void "project name cannot be blank"() {
        when: "set domain name to blank"
        domain.name = ""

        then: "domain fails to validate with blank error"
        !domain.validate(["name"])
        domain.errors["name"].code == "blank"
    }

    void "project name cannot exceed 100 characters"() {
        when: "for a string of 101 characters"
        String str = "a" * 101
        domain.name = str

        then: "name validation fails"
        !domain.validate(["name"])
        domain.errors["name"].code == "maxSize.exceeded"
    }

    void "project name validates with 100 characters"() {
        when: "for a string of 255 characters"
        String str = "a" * 100
        domain.name = str

        then: "name validation passes"
        domain.validate(["name"])
    }

    void "project code cannot exceed 3 characters"() {
        when: "for a string of 4 characters"
        String str = "a" * 4
        domain.code = str

        then: "code validation fails"
        !domain.validate(["code"])
        domain.errors["code"].code == "maxSize.exceeded"
    }

    void "project code validates with 3 characters"() {
        when: "for a string of 3 characters"
        String str = "a" * 3
        domain.code = str

        then: "code validation passes"
        domain.validate(["code"])
    }

    void "project code fails validation with 2 characters"() {
        when: "for a string of 2 characters"
        String str = "a" * 2
        domain.code = str

        then: "code validation fails"
        !domain.validate(["code"])
        domain.errors["code"].code == "minSize.notmet"
    }

    void "project code cannot be null"() {
        when: "set domain code to null"
        domain.code = null

        then: "domain fails to validate with nullable error"
        !domain.validate(["code"])
        domain.errors["code"].code == "nullable"
    }

    void "project code cannot be blank"() {
        when: "set domain code to blank"
        domain.code = ""

        then: "domain fails to validate with blank error"
        !domain.validate(["code"])
        domain.errors["code"].code == "blank"
    }
}
