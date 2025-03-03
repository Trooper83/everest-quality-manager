package com.manager.quality.everest.test.unit.testiteration

import com.manager.quality.everest.domains.IterationStep
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.domains.TestCycle
import com.manager.quality.everest.domains.TestIteration
import grails.test.hibernate.HibernateSpec
import spock.lang.Shared

class IterationStepHibernateSpec extends HibernateSpec {

    @Shared Person person
    @Shared Project project
    @Shared ReleasePlan releasePlan
    @Shared TestCase testCase
    @Shared TestCycle testCycle

    def setup() {
        person = new Person(email: "test@test.com", password: "!Password2022").save()
        project = new Project(name: "tc domain project", code: "tdp").save()
        releasePlan = new ReleasePlan(name: "releasing this", project: project, status: "ToDo", person: person).save()
        testCycle = new TestCycle(name: "name", releasePlan: releasePlan).save()
        testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
    }

    void "save cascades to step"() {
        given:
        def step = new IterationStep(action: "test", result: "result")

        expect:
        step.id == null

        when:
        new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [step], testCycle: testCycle).save()

        then:
        IterationStep.findById(step.id) != null
    }

    void "update cascades to step"() {
        given:
        def step = new IterationStep(act: "test", result: "result")
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [step], testCycle: testCycle).save()

        expect:
        IterationStep.findById(step.id).act == "test"

        when:
        iteration.steps.first().act = "edited"

        then:
        IterationStep.findById(step.id).act == "edited"
    }

    void "delete test iteration deletes step"() {
        given:
        def step = new IterationStep(act: "test", result: "result")
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [step], testCycle: testCycle).save()

        expect:
        step.id != null

        when:
        iteration.delete(flush: true)

        then:
        IterationStep.findById(step.id) == null
    }

    void "removeFrom test iteration deletes step"() {
        given:
        def step = new IterationStep(act: "test", result: "result")
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [step], testCycle: testCycle).save()

        expect:
        step.id != null

        when:
        iteration.removeFromSteps(step).save(flush: true)

        then:
        IterationStep.findById(step.id) == null
    }
}
