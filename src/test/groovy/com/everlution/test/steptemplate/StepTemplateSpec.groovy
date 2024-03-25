package com.everlution.test.steptemplate

import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.StepTemplate
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class StepTemplateSpec extends Specification implements DomainUnitTest<StepTemplate> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        def project = new Project(name: "tc domain project321", code: "td5").save()
        def person = new Person(email: "test@test.com", password: "test").save()
        new StepTemplate(name: "test name", act: "First Test step", result: "test", person: person, project: project).save()

        expect:
        StepTemplate.count() == 1
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.act = "Test case name"

        then:
        domain.act == "Test case name"
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.act == null
        System.identityHashCode(domain) != id
    }

    void "action can be null when result is not"() {
        when:
        domain.act = null
        domain.result = "test"

        then:
        domain.validate(["action"])
    }

    void "test action can be blank"() {
        when:
        domain.act = ""

        then:
        domain.validate(["action"])
    }

    void "validation fails if action and result are null"() {
        when:
        domain.act = null
        domain.result = null

        then:
        !domain.validate(["act"])
        domain.errors["act"].code == "validator.invalid"
        !domain.validate(["result"])
        domain.errors["result"].code == "validator.invalid"
    }

    void "test action cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.act = str

        then: "action validation fails"
        !domain.validate(["act"])
        domain.errors["act"].code == "maxSize.exceeded"
    }

    void "test action validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.act = str

        then: "action validation passes"
        domain.validate(["act"])
    }

    void "validation fails if result and action are null"() {
        when:
        domain.result = null
        domain.act = null

        then:
        !domain.validate(["result"])
        domain.errors["result"].code == "validator.invalid"
    }

    void "result can be null when action is not"() {
        when:
        domain.result = null
        domain.act = "test"

        then:
        domain.validate(["result"])
    }

    void "result can be blank"() {
        when:
        domain.result = ""

        then:
        domain.validate(["result"])
    }

    void "result cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.result = str

        then: "result validation fails"
        !domain.validate(["result"])
        domain.errors["result"].code == "maxSize.exceeded"
    }

    void "result validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.result = str

        then: "result validation passes"
        domain.validate(["result"])
    }

    void "project cannot be null"() {
        when:
        domain.project = null

        then:
        !domain.validate(["project"])
    }

    void "name cannot be null"() {
        when:
        domain.name = null

        then:
        !domain.validate(["name"])
    }

    void "name cannot be blank"() {
        when:
        domain.name = ""

        then:
        !domain.validate(["name"])
    }

    void "name cannot exceed 255 characters"() {
        when: "for a string of 256 characters"
        String str = "a" * 256
        domain.name = str

        then: "validation fails"
        !domain.validate(["name"])
        domain.errors["name"].code == "maxSize.exceeded"
    }

    void "name validates with 255 characters"() {
        when: "for a string of 255 characters"
        String str = "a" * 255
        domain.name = str

        then: "validation passes"
        domain.validate(["name"])
    }

    void "person cannot be null"() {
        when:
        domain.person = null

        then:
        !domain.validate(["person"])
    }

    void "dateCreated can be null"() {
        when:
        domain.dateCreated = null

        then:
        domain.validate(["dateCreated"])
    }

    void "lastUpdated can be null"() {
        when:
        domain.lastUpdated = null

        then:
        domain.validate(["lastUpdated"])
    }
}
