package com.everlution.test.automatedtest

import com.everlution.AutomatedTest
import com.everlution.Project
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class AutomatedTestSpec extends Specification implements DomainUnitTest<AutomatedTest> {

    @Shared int id

    void "instances are persisted"() {
        def project = new Project(name: "tc domain project321", code: "td5").save()
        new AutomatedTest(fullName: "First test", project: project).save()
        new AutomatedTest(fullName: "First test123", project: project).save()

        expect:
        AutomatedTest.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.fullName = "result"

        then:
        domain.fullName == "result"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.fullName == null
        System.identityHashCode(domain) != id
    }

    void "project cannot be null"() {
        when:
        domain.project = null

        then:
        !domain.validate(["project"])
        domain.errors["project"].code == "nullable"
    }

    void "fullName cannot be null"() {
        when:
        domain.fullName = null

        then:
        !domain.validate(["fullName"])
        domain.errors["fullName"].code == "nullable"
    }

    void "fullName cannot be blank"() {
        when:
        domain.fullName = ""

        then:
        !domain.validate(["fullName"])
        domain.errors["fullName"].code == "blank"
    }

    void "fullName cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.fullName = str

        then: "name validation fails"
        !domain.validate(["fullName"])
        domain.errors["fullName"].code == "maxSize.exceeded"
    }

    void "fullName validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.fullName = str

        then: "name validation passes"
        domain.validate(["fullName"])
    }

    void "name can be null"() {
        when:
        domain.name = null

        then:
        domain.validate(["name"])
    }

    void "name can be blank"() {
        when:
        domain.name = ""

        then:
        domain.validate(["name"])
    }

    void "name cannot exceed 256 characters"() {
        when:
        String str = "a" * 256
        domain.name = str

        then: "name validation fails"
        !domain.validate(["name"])
        domain.errors["name"].code == "maxSize.exceeded"
    }

    void "name validates with 255 characters"() {
        when: "for a string of 255 characters"
        String str = "a" * 255
        domain.name = str

        then: "name validation passes"
        domain.validate(["name"])
    }
}
