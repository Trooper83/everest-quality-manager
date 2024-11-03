package com.manager.quality.everest.test.unit.testcycle

import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.domains.TestCycle
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

    void "environ fails to validate when project null"() {
        when:
        domain.releasePlan = new ReleasePlan()
        domain.environ = new Environment(name: "test")

        then:
        !domain.validate(["environ"])
        domain.errors["environ"].code == "validator.invalid"
    }

    void "environ validates with environ in project"() {
        when:
        def e = new Environment(name: "test environ")
        def p = new Project(name: "testing project environ", code: "tpa", environments: [e])
        domain.releasePlan = new ReleasePlan(project: p)
        domain.environ = e

        then:
        domain.validate(["environ"])
    }

    void "environ fails to validate for project with no environs"() {
        when:
        def e = new Environment(name: "test environ")
        def p = new Project(name: "testing project environ", code: "tpa")
        domain.releasePlan = new ReleasePlan(project: p)
        domain.environ = e

        then:
        !domain.validate(["environ"])
        domain.errors["environ"].code == "validator.invalid"
    }

    void "environ not in project fails to validate for project with environs"() {
        when:
        def e = new Environment(name: "test environ").save()
        def p = new Project(name: "testing project environs", code: "tpa", environments: [new Environment(name: "test")])
        domain.releasePlan = new ReleasePlan(project: p)
        domain.environ = e

        then:
        !domain.validate(["environ"])
        domain.errors["environ"].code == "validator.invalid"
    }

    void "platform can be null"() {
        when:
        domain.platform = null

        then:
        domain.validate(["platform"])
    }

    void "platform fails to validate when project null"() {
        when:
        domain.releasePlan = new ReleasePlan()
        domain.platform = new Platform(name: "test")

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "platform validates with platform in project"() {
        when:
        def pl = new Platform(name: "test platform")
        def p = new Project(name: "testing project platform", code: "tpa", platforms: [pl])
        domain.releasePlan = new ReleasePlan(project: p)
        domain.platform = pl

        then:
        domain.validate(["platform"])
    }

    void "platform fails to validate for project with no platforms"() {
        when:
        def pl = new Platform(name: "test platform").save()
        def p = new Project(name: "testing project platform", code: "tpa")
        domain.releasePlan = new ReleasePlan(project: p)
        domain.platform = pl

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "platform not in project fails to validate for project with platforms"() {
        when:
        def pl = new Platform(name: "test platform").save()
        def p = new Project(name: "testing project platforms", code: "tpa", platforms: [new Platform(name: "test")])
        domain.releasePlan = new ReleasePlan(project: p)
        domain.platform = pl

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
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

    void "test case ids can be null"() {
        when:
        domain.testCaseIds = null

        then:
        domain.validate(["testCaseIds"])
    }
}
