package com.manager.quality.everest.test.unit.scenario

import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.Scenario
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class ScenarioSpec extends Specification implements DomainUnitTest<Scenario> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        def project = new Project(name: "tc domain project", code: "tdp")
        def person = new Person(email: "test@test.com", password: "pass")
        new Scenario(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        new Scenario(person: person, name: "Second Test Case", description: "test",
                executionMethod: "Automated", type: "API", project: project).save()

        expect:
        Scenario.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.name = "Scenario name"

        then:
        domain.name == "Scenario name"
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

    void "gherkin cannot exceed 2500 characters"() {
        when: "for a string of 2501 characters"
        String str = "a" * 2501
        domain.gherkin = str

        then: "gherkin validation fails"
        !domain.validate(["gherkin"])
        domain.errors["gherkin"].code == "maxSize.exceeded"
    }

    void "gherkin validates with 2500 characters"() {
        when: "for a string of 2500 characters"
        String str = "a" * 2500
        domain.gherkin = str

        then: "gherkin validation passes"
        domain.validate(["gherkin"])
    }

    void "gherkin can be null"() {
        when:
        domain.gherkin = null

        then:
        domain.validate(["gherkin"])
    }

    void "gherkin can be blank"() {
        when:
        domain.gherkin = ""

        then:
        domain.validate(["gherkin"])
    }

    void "execution method can be null"() {
        when:
        domain.executionMethod = null

        then:
        domain.validate(["executionMethod"])
    }

    void "test execution method can be blank"() {
        when:
        domain.executionMethod = ""

        then:
        domain.validate(["executionMethod"])
    }

    void "execution method value in list"(String value) {
        when:
        domain.executionMethod = value

        then:
        domain.validate(["executionMethod"])

        where:
        value       | _
        "Automated" | _
        "Manual"    | _
    }

    void "test execution method value not in list"() {
        when:
        domain.executionMethod = "test"

        then:
        !domain.validate(["executionMethod"])
        domain.errors["executionMethod"].code == "not.inList"
    }

    void "type can be null"() {
        when:
        domain.type = null

        then:
        domain.validate(["type"])
    }

    void "type can be blank"() {
        when:
        domain.type = ""

        then:
        domain.validate(["type"])
    }

    void "test type value in list"(String value) {
        when:
        domain.type = value

        then:
        domain.validate(["type"])

        where:
        value | _
        "API" | _
        "UI"  | _
    }

    void "test type value not in list"() {
        when:
        domain.type = "test"

        then:
        !domain.validate(["type"])
        domain.errors["type"].code == "not.inList"
    }

    void "platform can be null"() {
        when:
        domain.platform = null

        then:
        domain.validate(["platform"])
    }

    void "platform fails to validate when project null"() {
        when:
        domain.project = null
        domain.platform = new Platform(name: "test")

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "platform validates with platform in project"() {
        when:
        def a = new Platform(name: "test platform")
        def p = new Project(name: "testing project platforms", code: "tpa", platforms: [a]).save()
        domain.project = p
        domain.platform = a

        then:
        domain.validate(["platform"])
    }

    void "platform fails to validate for project with no platforms"() {
        when:
        def a = new Platform(name: "test platform").save()
        def p = new Project(name: "testing project platforms", code: "tpa").save()
        domain.project = p
        domain.platform = a

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "platform not in project fails to validate for project with platforms"() {
        when:
        def a = new Platform(name: "test platform").save()
        def p = new Project(name: "testing project platforms", code: "tpa", platforms: [new Platform(name: "test")]).save()
        domain.project = p
        domain.platform = a

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "person cannot be null"() {
        when:
        domain.person = null

        then:
        !domain.validate(["person"])
        domain.errors["person"].code == "nullable"
    }

    void "project cannot be null"() {
        when:
        domain.project = null

        then:
        !domain.validate(["project"])
        domain.errors["project"].code == "nullable"
    }

    void "platform can be null"() {
        when:
        domain.platform = null

        then:
        domain.validate(["platform"])
    }

    void "platform fails to validate when project null"() {
        when:
        domain.project = null
        domain.platform = new Platform(name: "test")

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "platform validates with platform in project"() {
        when:
        def a = new Platform(name: "test platform")
        def p = new Project(name: "testing project platforms", code: "tpa", platforms: [a]).save()
        domain.project = p
        domain.platform = a

        then:
        domain.validate(["platform"])
    }

    void "platform fails to validate for project with no platforms"() {
        when:
        def a = new Platform(name: "test platform").save()
        def p = new Project(name: "testing project platforms", code: "tpa").save()
        domain.project = p
        domain.platform = a

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "platform not in project fails to validate for project with platforms"() {
        when:
        def a = new Platform(name: "test platform").save()
        def p = new Project(name: "testing project platforms", code: "tpa", platforms: [new Platform(name: "test")]).save()
        domain.project = p
        domain.platform = a

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "environment can be null"() {
        when:
        domain.environments = null

        then:
        domain.validate(["environments"])
    }

    void "environment fails to validate when project null"() {
        when:
        domain.project = null
        domain.environments = [new Environment(name: "test")]

        then:
        !domain.validate(["environments"])
        domain.errors["environments"].code == "validator.invalid"
    }

    void "environment validates with environment in project"() {
        when:
        def e = new Environment(name: "test env")
        def p = new Project(name: "testing project platforms", code: "tpa", environments: [e]).save()
        domain.project = p
        domain.environments = [e]

        then:
        domain.validate(["environments"])
    }

    void "environment validates with multiple environments in project"() {
        when:
        def e = new Environment(name: "test env")
        def en = new Environment(name: "test environment")
        def p = new Project(name: "testing project platforms", code: "tpa", environments: [e, en]).save()
        domain.project = p
        domain.environments = [e, en]

        then:
        domain.validate(["environments"])
    }

    void "environment fails to validate with multiple environments only one in project"() {
        when:
        def e = new Environment(name: "test env")
        def en = new Environment(name: "test environment")
        def p = new Project(name: "testing project platforms", code: "tpa", environments: [e]).save()
        domain.project = p
        domain.environments = [e, en]

        then:
        !domain.validate(["environments"])
        domain.errors["environments"].code == "validator.invalid"
    }

    void "environment fails to validate for project with no environments"() {
        when:
        def e = new Environment(name: "test env").save()
        def p = new Project(name: "testing project platforms", code: "tpa").save()
        domain.project = p
        domain.environments = [e]

        then:
        !domain.validate(["environments"])
        domain.errors["environments"].code == "validator.invalid"
    }

    void "environment not in project fails to validate for project with environments"() {
        when:
        def e = new Environment(name: "test env").save()
        def p = new Project(name: "testing project platforms", code: "tpa", environments: [new Environment(name: "failed")]).save()
        domain.project = p
        domain.environments = [e]

        then:
        !domain.validate(["environments"])
        domain.errors["environments"].code == "validator.invalid"
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