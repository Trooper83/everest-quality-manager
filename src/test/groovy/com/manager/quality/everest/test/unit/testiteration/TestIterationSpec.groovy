package com.manager.quality.everest.test.unit.testiteration

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.domains.TestCycle
import com.manager.quality.everest.domains.TestIteration
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestIterationSpec extends Specification implements DomainUnitTest<TestIteration> {

    @Shared int id

    void "instances are persisted"() {
        setup:
        def person = new Person(email: "test@test.com", password: "test")
        def project = new Project(name: "tc domain project", code: "tdp")
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def releasePlan = new ReleasePlan(name: "releasing this", project: project, person: person).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        new TestIteration(name: "test name", testCase: testCase, steps: [], testCycle: testCycle).save()
        new TestIteration(name: "test name123", testCase: testCase, steps: [], testCycle: testCycle).save()

        expect:
        TestIteration.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.name = "test name"

        then:
        domain.name == "test name"
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

    void "steps cannot be null"() {
        when:
        domain.steps = null

        then:
        !domain.validate(["steps"])
        domain.errors["steps"].code == "nullable"
    }

    void "steps validates with empty list"() {
        when:
        domain.steps = []

        then:
        domain.validate(["steps"])
    }

    void "test case cannot be null"() {
        when:
        domain.testCase = null

        then:
        !domain.validate(["testCase"])
        domain.errors["testCase"].code == "nullable"
    }

    void "test case validates with valid test case"() {
        when:
        domain.testCase = new TestCase()

        then:
        domain.validate(["testCase"])
    }

    void "test cycle cannot be null"() {
        when:
        domain.testCycle = null

        then:
        !domain.validate(["testCycle"])
        domain.errors["testCycle"].code == "nullable"
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

    void "results can be null"() {
        when:
        domain.results = null

        then:
        domain.validate(["results"])
    }

    void "lastExecuted can be null"() {
        when:
        domain.lastExecuted = null

        then:
        domain.validate(["dateCreated"])
    }

    void "lastResult can be blank"() {
        when:
        domain.lastResult = ""

        then:
        domain.validate(["lastResult"])
    }

    void "lastResult can be null"() {
        when:
        domain.lastResult = null

        then:
        domain.validate(["lastResult"])
    }

    void "lastResult validates with value in list"(String value) {
        when:
        domain.lastResult = value

        then:
        domain.validate(["lastResult"])

        where:
        value << ["SKIPPED", "PASSED", "FAILED"]
    }

    void "lastResult fails validation with value not in list"() {
        when:
        domain.lastResult = "test"

        then:
        !domain.validate(["lastResult"])
        domain.errors["lastResult"].code == "not.inList"
    }

    void "lastExecutedBy can be null"() {
        when:
        domain.lastExecutedBy = null

        then:
        domain.validate(["lastExecutedBy"])
    }
}
