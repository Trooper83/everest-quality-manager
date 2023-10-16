package com.everlution.test.project

import com.everlution.Area
import com.everlution.Environment
import com.everlution.Project
import com.everlution.TestGroup
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

    void "code is upper cased at binding"() {
        setup:
        def p = new Project(name: "first project999", code: "zzz").save()

        expect:
        p.code == "ZZZ"
    }

    void "code persists with numbers"() {
        setup:
        def p = new Project(name: "first project321", code: "1zz").save()

        expect:
        p.id != null
    }

    void "name is capitalized at binding"() {
        setup:
        def p = new Project(name: "capitalized", code: "zzz").save()

        expect:
        p.name == "Capitalized"
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

    void "project code cannot exceed 5 characters"() {
        when: "for a string of 4 characters"
        String str = "a" * 6
        domain.code = str

        then: "code validation fails"
        !domain.validate(["code"])
        domain.errors["code"].code == "maxSize.exceeded"
    }

    void "project code validates with 3-5 characters"(int number) {
        when: "for a string of 3 characters"
        String str = "a" * number
        domain.code = str

        then: "code validation passes"
        domain.validate(["code"])

        where:
        number << [3,4,5]
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

    void "save project cascades to area"() {
        expect:
        Area.count() == 0

        when: "project with valid area params"
        new Project(name: "Cascades To Area", code: "CTA", areas: [new Area(name: "area name")]).save()

        then: "area is saved"
        Area.count() == 1
    }

    void "project name must be unique"() {
        given: "a project instance"
        new Project(name: "test", code: "cod").save()

        when: "create a project with same name"
        def failed = new Project(name: "test", code: "san")

        then: "name fails validation"
        !failed.validate(["name"])

        and: "unique error"
        failed.errors["name"].code == "unique"

        and: "save fails"
        !failed.save()
        Project.count() == old(Project.count())
    }

    void "project code must be unique"() {
        given: "a project instance"
        new Project(name: "test", code: "cod").save()

        when: "create a project with same code"
        def failed = new Project(name: "testing", code: "cod")

        then: "code fails validation"
        !failed.validate(["code"])

        and: "unique error"
        failed.errors["code"].code == "unique"

        and: "save fails"
        !failed.save()
        Project.count() == old(Project.count())
    }

    void "project cannot have duplicate area names"() {
        when: "project with duplicate area params"
        domain.areas = [new Area(name: "name"), new Area(name: "name")]

        then: "project validation fails"
        !domain.validate(["areas"])
        domain.errors["areas"].code == "validator.invalid"
    }

    void "project validates with non-duplicate area names"() {
        when: "project with duplicate area params"
        domain.areas = [new Area(name: "name"), new Area(name: "name123")]

        then: "project validation fails"
        domain.validate(["areas"])
    }

    void "areas can be null"() {
        when: "area is null"
        domain.areas = null

        then: "domain validates"
        domain.validate(["areas"])
    }

    void "save project cascades to environment"() {
        expect:
        Environment.count() == 0

        when: "project with valid env params"
        new Project(name: "Cascades To Env", code: "CTE", environments: [new Environment(name: "area name")]).save()

        then: "env is saved"
        Environment.count() == 1
    }

    void "project cannot have duplicate environment names"() {
        when: "project with duplicate env params"
        domain.environments = [new Environment(name: "name"), new Environment(name: "name")]

        then: "project validation fails"
        !domain.validate(["environments"])
        domain.errors["environments"].code == "validator.invalid"
    }

    void "project validates with non-duplicate environment names"() {
        when: "project with duplicate env params"
        domain.environments = [new Environment(name: "name"), new Environment(name: "name123")]

        then: "project validates"
        domain.validate(["environments"])
    }

    void "environments can be null"() {
        when: "environment is null"
        domain.environments = null

        then: "domain validates"
        domain.validate(["environments"])
    }

    void "save project cascades to test group"() {
        expect:
        TestGroup.count() == 0

        when: "project with valid group params"
        new Project(name: "Cascades To Env", code: "CTE", testGroups: [new TestGroup(name: "test")]).save()

        then: "group is saved"
        TestGroup.count() == 1
    }

    void "project cannot have duplicate group names"() {
        when: "project with duplicate group params"
        domain.testGroups = [new TestGroup(name: "name"), new TestGroup(name: "name")]

        then: "project validation fails"
        !domain.validate(["testGroups"])
        domain.errors["testGroups"].code == "validator.invalid"
    }

    void "project validates with non-duplicate test group names"() {
        when: "project with duplicate group params"
        domain.testGroups = [new TestGroup(name: "name"), new TestGroup(name: "name123")]

        then: "domain validates"
        domain.validate(["testGroups"])
    }

    void "groups can be null"() {
        when: "property is null"
        domain.testGroups = null

        then: "domain validates"
        domain.validate(["testGroups"])
    }
}
