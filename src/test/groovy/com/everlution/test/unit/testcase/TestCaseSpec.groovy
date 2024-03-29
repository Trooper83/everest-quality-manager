package com.everlution.test.unit.testcase

import com.everlution.domains.Area
import com.everlution.domains.Environment
import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.TestCase
import com.everlution.domains.TestGroup
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestCaseSpec extends Specification implements DomainUnitTest<TestCase> {

    @Shared int id

    void "test instances are persisted"() {
        setup:
        def person = new Person(email: "test@test.com", password: "test")
        def project = new Project(name: "tc domain project", code: "tdp")
        new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        new TestCase(person: person,name: "Second Test Case", description: "test",
                executionMethod: "Automated", type: "API", project: project).save()

        expect:
        TestCase.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.name = "Test case name"

        then:
        domain.name == "Test case name"
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

    void "steps can be null"() {
        when:
        domain.steps = null

        then:
        domain.validate(["steps"])
    }

    void "execution method can be null"() {
        when:
        domain.executionMethod = null

        then:
        domain.validate(["executionMethod"])
    }

    void "execution method can be blank"() {
        when:
        domain.executionMethod = ""

        then:
        domain.validate(["executionMethod"])
    }

    void "test execution method value in list"(String value) {
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

    void "type cannot be blank"() {
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

    void "test group can be null"() {
        when:
        domain.testGroups = null

        then:
        domain.validate(["testGroups"])
    }

    void "test group fails to validate when project null"() {
        when:
        domain.project = null
        domain.testGroups = [new TestGroup(name: "test")]

        then:
        !domain.validate(["testGroups"])
        domain.errors["testGroups"].code == "validator.invalid"
    }

    void "test group validates with group in project"() {
        when:
        def g = new TestGroup(name: "test group")
        def p = new Project(name: "testing project groups", code: "tpg", testGroups: [g]).save()
        domain.project = p
        domain.testGroups = [g]

        then:
        domain.validate(["testGroups"])
    }

    void "test group validates with multiple group in project"() {
        when:
        def g = new TestGroup(name: "test group")
        def gr = new TestGroup(name: "test grouping")
        def p = new Project(name: "testing project areas", code: "tpa", testGroups: [g, gr]).save()
        domain.project = p
        domain.testGroups = [g, gr]

        then:
        domain.validate(["testGroups"])
    }

    void "test groups fails to validate with multiple groups only one in project"() {
        when:
        def g = new TestGroup(name: "test group")
        def gr = new TestGroup(name: "test grouping")
        def p = new Project(name: "testing project areas", code: "tpa", testGroups: [g]).save()
        domain.project = p
        domain.testGroups = [g, gr]

        then:
        !domain.validate(["testGroups"])
        domain.errors["testGroups"].code == "validator.invalid"
    }

    void "test groups fails to validate for project with no test groups"() {
        when:
        def g = new TestGroup(name: "test").save()
        def p = new Project(name: "testing project areas", code: "tpa").save()
        domain.project = p
        domain.testGroups = [g]

        then:
        !domain.validate(["testGroups"])
        domain.errors["testGroups"].code == "validator.invalid"
    }

    void "test groups not in project fails to validate for project with test groups"() {
        when:
        def g = new TestGroup(name: "test group").save()
        def p = new Project(name: "testing project areas", code: "tpa", testGroups: [new TestGroup(name: "failed")]).save()
        domain.project = p
        domain.testGroups = [g]

        then:
        !domain.validate(["testGroups"])
        domain.errors["testGroups"].code == "validator.invalid"
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

    void "verify can be null"() {
        when:
        domain.verify = null

        then:
        domain.validate(["verify"])
    }

    void "verify can be blank"() {
        when:
        domain.verify = ""

        then:
        domain.validate(["verify"])
    }

    void "verify cannot exceed 500 characters"() {
        when: "for a string of 501 characters"
        String str = "a" * 501
        domain.verify = str

        then: "validation fails"
        !domain.validate(["verify"])
        domain.errors["verify"].code == "maxSize.exceeded"
    }

    void "verify validates with 500 characters"() {
        when: "for a string of 500 characters"
        String str = "a" * 500
        domain.verify = str

        then: "description validation passes"
        domain.validate(["verify"])
    }
}
