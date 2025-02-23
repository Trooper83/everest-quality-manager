package com.manager.quality.everest.test.unit.bug

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class BugSpec extends Specification implements DomainUnitTest<Bug> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        def project = new Project(name: "tc domain project321", code: "td5").save()
        def person = new Person(email: "test@test.com", password: "test")
        new Bug(person: person, name: "First Bug", description: "test", project: project, status: "Open",
                actual: "actual", expected: "expected").save()
        new Bug(person: person, name: "Second Bug", description: "test", project: project, status: "Open",
                actual: "actual", expected: "expected").save()

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

    void "person cannot be null"() {
        when:
        domain.person = null

        then:
        !domain.validate(["person"])
        domain.errors["person"].code == "nullable"
    }

    void "area can be null"() {
        when:
        domain.area = null

        then:
        domain.validate(["area"])
    }

    void "area fails to validate when project null"() {
        when:
        domain.project = null
        domain.area = new Area(name: "test")

        then:
        !domain.validate(["area"])
        domain.errors["area"].code == "validator.invalid"
    }

    void "area validates with area in project"() {
        when:
        def a = new Area(name: "test area")
        def p = new Project(name: "testing project areas", code: "tpa", areas: [a]).save()
        domain.project = p
        domain.area = a

        then:
        domain.validate(["area"])
    }

    void "area fails to validate for project with no areas"() {
        when:
        def a = new Area(name: "test area").save()
        def p = new Project(name: "testing project areas", code: "tpa").save()
        domain.project = p
        domain.area = a

        then:
        !domain.validate(["area"])
        domain.errors["area"].code == "validator.invalid"
    }

    void "area not in project fails to validate for project with areas"() {
        when:
        def a = new Area(name: "test area").save()
        def p = new Project(name: "testing project areas", code: "tpa", areas: [new Area(name: "test")]).save()
        domain.project = p
        domain.area = a

        then:
        !domain.validate(["area"])
        domain.errors["area"].code == "validator.invalid"
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
        def p = new Project(name: "testing project areas", code: "tpa", environments: [e]).save()
        domain.project = p
        domain.environments = [e]

        then:
        domain.validate(["environments"])
    }

    void "environment fails to validate for project with no environments"() {
        when:
        def e = new Environment(name: "test env").save()
        def p = new Project(name: "testing project areas", code: "tpa").save()
        domain.project = p
        domain.environments = [e]

        then:
        !domain.validate(["environments"])
        domain.errors["environments"].code == "validator.invalid"
    }

    void "environment not in project fails to validate for project with environments"() {
        when:
        def e = new Environment(name: "test env").save()
        def p = new Project(name: "testing project areas", code: "tpa", environments: [new Environment(name: "failed")]).save()
        domain.project = p
        domain.environments = [e]

        then:
        !domain.validate(["environments"])
        domain.errors["environments"].code == "validator.invalid"
    }

    void "environment validates with multiple environments in project"() {
        when:
        def e = new Environment(name: "test env")
        def en = new Environment(name: "test environment")
        def p = new Project(name: "testing project areas", code: "tpa", environments: [e, en]).save()
        domain.project = p
        domain.environments = [e, en]

        then:
        domain.validate(["environments"])
    }

    void "environment fails to validate with multiple environments only one in project"() {
        when:
        def e = new Environment(name: "test env")
        def en = new Environment(name: "test environment")
        def p = new Project(name: "testing project areas", code: "tpa", environments: [e]).save()
        domain.project = p
        domain.environments = [e, en]

        then:
        !domain.validate(["environments"])
        domain.errors["environments"].code == "validator.invalid"
    }

    void "project cannot be null"() {
        when:
        domain.project = null

        then:
        !domain.validate(["project"])
        domain.errors["project"].code == "nullable"
    }

    void "steps can be null"() {
        when:
        domain.steps = null

        then:
        domain.validate(["steps"])
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
        def pl = new Platform(name: "test platform")
        def p = new Project(name: "testing project platform", code: "tpa", platforms: [pl]).save()
        domain.project = p
        domain.platform = pl

        then:
        domain.validate(["platform"])
    }

    void "platform fails to validate for project with no platforms"() {
        when:
        def pl = new Platform(name: "test platform").save()
        def p = new Project(name: "testing project platform", code: "tpa").save()
        domain.project = p
        domain.platform = pl

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
    }

    void "platform not in project fails to validate for project with platforms"() {
        when:
        def pl = new Platform(name: "test platform").save()
        def p = new Project(name: "testing project platforms", code: "tpa", platforms: [new Platform(name: "test")]).save()
        domain.project = p
        domain.platform = pl

        then:
        !domain.validate(["platform"])
        domain.errors["platform"].code == "validator.invalid"
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
        domain.status = ""

        then:
        !domain.validate(["status"])
        domain.errors["status"].code == "blank"
    }

    void "status value in list"(String value) {
        when:
        domain.status = value

        then:
        domain.validate(["status"])

        where:
        value << ["Open", "Closed", "Fixed"]
    }

    void "status value not in list"() {
        when:
        domain.status = "test"

        then:
        !domain.validate(["status"])
        domain.errors["status"].code == "not.inList"
    }

    void "actual can be null"() {
        when:
        domain.actual = null

        then:
        domain.validate(["actual"])
    }

    void "actual can be blank"() {
        when:
        domain.actual = ""

        then:
        domain.validate(["actual"])
    }

    void "actual cannot exceed 500 characters"() {
        when: "for a string of 256 characters"
        String str = "a" * 501
        domain.actual = str

        then: "actual validation fails"
        !domain.validate(["actual"])
        domain.errors["actual"].code == "maxSize.exceeded"
    }

    void "actual validates with 500 characters"() {
        when: "for a string of 255 characters"
        String str = "a" * 500
        domain.actual = str

        then: "actual validation passes"
        domain.validate(["actual"])
    }

    void "expected can be null"() {
        when:
        domain.expected = null

        then:
        domain.validate(["expected"])
    }

    void "expected can be blank"() {
        when:
        domain.expected = ""

        then:
        domain.validate(["expected"])
    }

    void "expected cannot exceed 500 characters"() {
        when: "for a string of 256 characters"
        String str = "a" * 501
        domain.expected = str

        then: "expected validation fails"
        !domain.validate(["expected"])
        domain.errors["expected"].code == "maxSize.exceeded"
    }

    void "expected validates with 500 characters"() {
        when: "for a string of 255 characters"
        String str = "a" * 500
        domain.expected = str

        then: "expected validation passes"
        domain.validate(["expected"])
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

    void "notes cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.notes = str

        then: "validation fails"
        !domain.validate(["notes"])
        domain.errors["notes"].code == "maxSize.exceeded"
    }

    void "notes validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.notes = str

        then: "description validation passes"
        domain.validate(["notes"])
    }
}
