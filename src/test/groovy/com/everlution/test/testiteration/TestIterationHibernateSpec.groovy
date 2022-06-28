package com.everlution.test.testiteration

import com.everlution.IterationStep
import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestIteration
import grails.test.hibernate.HibernateSpec
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
        releasePlan = new ReleasePlan(name: "releasing this", project: project).save()
        testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
    }

    void "delete iteration with test case deletes iteration"() {
        given:
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [],
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
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo",
                steps: [testStep, testStep1, testStep2], testCycle: testCycle).save()

        when:
        def found = TestIteration.findById(iteration.id)

        then:
        found.steps[0].id == testStep.id
        found.steps[1].id == testStep1.id
        found.steps[2].id == testStep2.id
    }
}
