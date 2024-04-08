package com.everlution.test.unit.testiteration

import com.everlution.domains.IterationStep
import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.ReleasePlan
import com.everlution.domains.TestCase
import com.everlution.domains.TestCycle
import com.everlution.domains.TestIteration
import com.everlution.domains.TestIterationResult
import grails.test.hibernate.HibernateSpec
import grails.validation.ValidationException
import spock.lang.Shared

class TestIterationHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [TestCase, TestIteration] }

    @Shared Person person
    @Shared Project project
    @Shared ReleasePlan releasePlan
    @Shared TestCase testCase
    @Shared TestCycle testCycle

    def setup() {
        person = new Person(email: "test@test.com", password: "!Password2022").save()
        project = new Project(name: "tc domain project", code: "tdp").save()
        releasePlan = new ReleasePlan(name: "releasing this", project: project, status: "ToDo",
                person: person).save()
        testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
    }

    void "delete iteration with test case deletes iteration"() {
        given:
        def iteration = new TestIteration(name: "test name", testCase: testCase, steps: [],
            testCycle: testCycle).save()

        expect:
        TestIteration.findById(iteration.id) != null

        when:
        iteration.delete(flush: true)

        then:
        TestIteration.findById(iteration.id) == null
    }

    void "step order persists"() {
        given:
        def testStep = new IterationStep(action: "do something", result: "something happened123")
        def testStep1 = new IterationStep(action: "I did something", result: "something happened231")
        def testStep2 = new IterationStep(action: "something happened", result: "something happened321")
        def iteration = new TestIteration(name: "test name", testCase: testCase,
                steps: [testStep, testStep1, testStep2], testCycle: testCycle).save()

        when:
        def found = TestIteration.findById(iteration.id)

        then:
        found.steps[0].id == testStep.id
        found.steps[1].id == testStep1.id
        found.steps[2].id == testStep2.id
    }

    void "save cascades to results"() {
        when:
        def r = new TestIterationResult(person: person, result: "PASSED")
        new TestIteration(name: "test iteration name", testCase: testCase,
                steps: [], testCycle: testCycle, results: [r]).save()

        then:
        r.id != null
    }

    void "delete cascades to results"() {
        given:
        def r = new TestIterationResult(person: person, result: "PASSED")
        def t = new TestIteration(name: "test iteration name", testCase: testCase,
                steps: [], testCycle: testCycle, results: [r]).save()

        expect:
        TestIterationResult.get(r.id) != null

        when:
        t.delete()

        then:
        TestIterationResult.get(r.id) == null
    }

    void "result order persists"() {
        given:
        def r = new TestIterationResult(person: person, result: "PASSED")
        def r1 = new TestIterationResult(person: person, result: "FAILED")
        def r2 = new TestIterationResult(person: person, result: "SKIPPED")
        def t = new TestIteration(name: "test iteration name", testCase: testCase,
                steps: [], testCycle: testCycle, results: [r, r1, r2]).save()

        when:
        def found = TestIteration.get(t.id)

        then:
        found.results[0].id == r.id
        found.results[1].id == r1.id
        found.results[2].id == r2.id
    }
}
