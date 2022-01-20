package com.everlution.test.testiteration

import com.everlution.IterationStep
import com.everlution.Person
import com.everlution.Project
import com.everlution.TestCase
import com.everlution.TestIteration
import grails.test.hibernate.HibernateSpec
import spock.lang.Shared

class IterationStepHibernateSpec extends HibernateSpec {

    @Shared Person person
    @Shared Project project
    @Shared TestCase testCase

    def setup() {
        person = new Person(email: "test@test.com", password: "test").save()
        project = new Project(name: "tc domain project", code: "tdp").save()
        testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
    }

    void "save cascades to step"() {
        given:
        def step = new IterationStep(action: "test", result: "result")

        expect:
        step.id == null

        when:
        new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [step]).save()

        then:
        IterationStep.findById(step.id) != null
    }

    void "update cascades to step"() {
        given:
        def step = new IterationStep(action: "test", result: "result")
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [step]).save()

        expect:
        IterationStep.findById(step.id).action == "test"

        when:
        iteration.steps.first().action = "edited"

        then:
        IterationStep.findById(step.id).action == "edited"
    }

    void "delete test iteration deletes step"() {
        given:
        def step = new IterationStep(action: "test", result: "result")
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [step]).save()

        expect:
        step.id != null

        when:
        iteration.delete(flush: true)

        then:
        IterationStep.findById(step.id) == null
    }

    void "removeFrom test iteration deletes step"() {
        given:
        def step = new IterationStep(action: "test", result: "result")
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [step]).save()

        expect:
        step.id != null

        when:
        iteration.removeFromSteps(step).save(flush: true)

        then:
        IterationStep.findById(step.id) == null
    }
}
