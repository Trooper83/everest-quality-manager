package com.everlution.test.testcycle

import com.everlution.Environment
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCycle
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestCycleSpec extends Specification implements DomainUnitTest<TestCycle> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        def project = new Project(name: "release name", code: "doc").save()
        def plan = new ReleasePlan(name: "release plan name", project: project).save()
        new TestCycle(name: "First Test Case", releasePlan: plan).save()
        new TestCycle(name: "Second Test Case", releasePlan: plan).save()

        expect:
        TestCycle.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.name = "Test cycle name"

        then:
        domain.name == "Test cycle name"
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

    void "environ can be null"() {
        when:
        domain.environ = null

        then:
        domain.validate(["environ"])
    }

    void "environ validates with environment in releasePlan.project"() {
        when:
        def e = new Environment(name: "test env")
        def p = new Project(name: "testing project areas", code: "tpa", environments: [e]).save()
        def plan = new ReleasePlan(name: "release plan", project: p).save()

        domain.releasePlan = plan
        domain.environ = e

        then:
        domain.validate(["environ"])
    }

    void "environ fails to validate for project with no environments"() {
        when:
        def e = new Environment(name: "test env").save()
        def p = new Project(name: "testing project areas", code: "tpa").save()
        def plan = new ReleasePlan(name: "release plan", project: p).save()

        domain.releasePlan = plan
        domain.environ = e

        then:
        !domain.validate(["environ"])
        domain.errors["environ"].code == "validator.invalid"
    }

    void "environ not in project fails to validate for project with environments"() {
        when:
        def e = new Environment(name: "test env").save()
        def p = new Project(name: "testing project areas", code: "tpa", environments: [new Environment(name: "failed")]).save()
        def plan = new ReleasePlan(name: "release plan", project: p).save()

        domain.releasePlan = plan
        domain.environ = e

        then:
        !domain.validate(["environ"])
        domain.errors["environ"].code == "validator.invalid"
    }

    void "platform can be null"() {
        when: "property is null"
        domain.platform = null

        then: "domain validates"
        domain.validate(["platform"])
    }

    void "platform can be blank"() {
        when: "property is blank"
        domain.platform = ""

        then: "domain validates"
        domain.validate(["platform"])
    }

    void "platform validates with value in list"(String value) {
        when: "value in list"
        domain.platform = value

        then: "domain validates"
        domain.validate(["platform"])

        where:
        value << ["Android", "iOS", "Web"]
    }

    void "platform fails validation with value not in list"() {
        when: "value not in list"
        domain.platform = "test"

        then: "validation fails"
        !domain.validate(["platform"])
        domain.errors["platform"].code == "not.inList"
    }

    void "release plan cannot be null"() {
        when:
        domain.name = null

        then:
        !domain.validate(["releasePlan"])
        domain.errors["releasePlan"].code == "nullable"
    }

    void "test iterations can be null"() {
        when:
        domain.testIterations = null

        then:
        domain.validate(["testIterations"])
    }
}
