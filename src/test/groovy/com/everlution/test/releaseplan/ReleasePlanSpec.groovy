package com.everlution.test.releaseplan

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class ReleasePlanSpec extends Specification implements DomainUnitTest<ReleasePlan> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        def person = new Person(email: "test@test.com", password: "test")
        def project = new Project(name: "rps domain project", code: "rps")
        new ReleasePlan(name: "First Test Case", project: project, status: "ToDo", person: person).save()
        new ReleasePlan(name: "Second Test Case", project: project, status: "ToDo", person: person).save()

        expect:
        ReleasePlan.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.name = "name"

        then:
        domain.name == "name"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.name == null
        System.identityHashCode(domain) != id
    }

    void "name cannot be null"() {
        when:
        domain.name = null

        then:
        !domain.validate(["name"])
        domain.errors["name"].code == "nullable"
    }

    void "name cannot be blank"() {
        when:
        domain.name = ""

        then:
        !domain.validate(["name"])
        domain.errors["name"].code == "blank"
    }

    void "name cannot exceed 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 501
        domain.name = str

        then: "name validation fails"
        !domain.validate(["name"])
        domain.errors["name"].code == "maxSize.exceeded"
    }

    void "test name validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.name = str

        then: "name validation passes"
        domain.validate(["name"])
    }

    void "dateCreated can be null"() {
        when: "property null"
        domain.dateCreated = null

        then: "validation passes"
        domain.validate(["dateCreated"])
    }

    void "lastUpdated can be null"() {
        when: "property null"
        domain.lastUpdated = null

        then: "validation passes"
        domain.validate(["lastUpdated"])
    }

    void "person cannot be null"() {
        when: "property null"
        domain.person = null

        then: "validation passes"
        !domain.validate(["person"])
    }

    void "test cycles can be null"() {
        when: "property null"
        domain.testCycles = null

        then: "validation passes"
        domain.validate(["testCycles"])
    }

    void "project cannot be null"() {
        when:
        domain.project = null

        then:
        !domain.validate(["project"])
        domain.errors["project"].code == "nullable"
    }

    void "planned date can be null"() {
        when:
        domain.plannedDate = null

        then:
        domain.validate(["plannedDate"])
    }

    void "release date can be null"() {
        when:
        domain.releaseDate = null

        then:
        domain.validate(["releaseDate"])
    }

    void "status cannot be null"() {
        when:
        domain.status = null

        then:
        !domain.validate(["status"])
        domain.errors["status"].code == "nullable"
    }

    void "status cannot be blank"() {
        when:
        domain.status = ''

        then:
        !domain.validate(["status"])
        domain.errors["status"].code == "blank"
    }

    void "status validates with value in list"(String value) {
        when:
        domain.status = value

        then:
        domain.validate(["status"])

        where:
        value << ["ToDo", "Planning", "In Progress", "Released", "Canceled"]
    }

    void "status does not validate with value not in list"() {
        when:
        domain.status = 'test'

        then:
        !domain.validate(["status"])
        domain.errors["status"].code == "not.inList"
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
        when:
        String str = "a" * 1001
        domain.notes = str

        then:
        !domain.validate(["notes"])
        domain.errors["notes"].code == "maxSize.exceeded"
    }

    void "notes validates with 1000 characters"() {
        when:
        String str = "a" * 1000
        domain.notes = str

        then:
        domain.validate(["notes"])
    }
}
