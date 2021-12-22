package com.everlution.test.release

import com.everlution.Project
import com.everlution.ReleasePlan
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class ReleasePlanSpec extends Specification implements DomainUnitTest<ReleasePlan> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        def project = new Project(name: "rps domain project", code: "rps")
        new ReleasePlan(name: "First Test Case", project: project).save()
        new ReleasePlan(name: "Second Test Case", project: project).save()

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

    void "date created can be null"() {
        when: "property null"
        domain.dateCreated = null

        then: "validation passes"
        domain.validate(["dateCreated"])
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
}
