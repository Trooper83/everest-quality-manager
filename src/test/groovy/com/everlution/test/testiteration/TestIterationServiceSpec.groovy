package com.everlution.test.testiteration

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestIteration
import com.everlution.TestIterationService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class TestIterationServiceSpec extends Specification implements ServiceUnitTest<TestIterationService>, DataTest {

    @Shared Person person
    @Shared Project project
    @Shared TestCase testCase

    def setupSpec() {
        mockDomains(Person, Project, TestCase, TestIteration)
    }

    def setup() {
        person = new Person(email: "test@test.com", password: "test").save()
        project = new Project(name: "tc domain project", code: "tdp").save()
        testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
    }

    void "get with valid id returns instance"() {
        when:
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        new TestIteration(name: "name", testCase: testCase, steps: [], result: "ToDo", testCycle: testCycle).save()

        then:
        service.get(1) instanceof TestIteration
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "read with valid id returns instance"() {
        when:
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        new TestIteration(name: "name", testCase: testCase, steps: [], result: "ToDo", testCycle: testCycle).save()

        then:
        service.read(1) instanceof TestIteration
    }

    void "read with invalid id returns null"() {
        expect:
        service.read(999999) == null
    }

    void "save with valid iteration returns instance"() {
        given:
        def person = new Person(email: "test@test.com", password: "test").save()
        def project = new Project(name: "tc domain project", code: "tdp").save()
        def releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        def testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [],
                testCycle: testCycle)

        when:
        def saved = service.save(iteration)

        then:
        saved instanceof TestIteration
    }

    void "save with invalid iteration throws validation exception"() {
        given:
        def iteration = new TestIteration()

        when:
        service.save(iteration)

        then:
        thrown(ValidationException)
    }
}
