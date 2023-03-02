package com.everlution.test.step

import com.everlution.Bug
import com.everlution.Person
import com.everlution.Project
import com.everlution.Step
import com.everlution.TestCase
import grails.test.hibernate.HibernateSpec

class StepHibernateSpec extends HibernateSpec {

    void "test date created auto generated"() {
        when:
        Step testStep = new Step(action: "do something", result: "something happened").save()

        then:
        testStep.dateCreated != null
    }

    void "delete step does not cascade to case"() {
        given: "valid domain instances"
        Project project = new Project(name: "TestStep Cascade Project", code: "TCP").save()
        Step testStep = new Step(action: "do something", result: "something happened")
        def person = new Person(email: "test@test.com", password: "!Password2022").save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", project: project).addToSteps(testStep)
        testCase.save()

        expect:
        testCase.steps.size() == 1
        testStep.id != null
        testCase.id != null

        when: "delete test step"
        testCase.removeFromSteps(testStep)
        testStep.delete()
        sessionFactory.currentSession.flush()

        then: "test case is still found"
        Step.findById(testStep.id) == null
        def tc = TestCase.findById(testCase.id)
        tc.steps.size() == 0
    }

    void "delete step does not cascade to bug"() {
        given: "valid domain instances"
        Project project = new Project(name: "TestStep Cascade Project", code: "TCC").save()
        Step testStep = new Step(action: "do something", result: "something happened")
        def person = new Person(email: "test@test.com", password: "!Password2022").save()
        Bug bug = new Bug(person: person, name: "test", description: "desc",
               project: project, status: "Open", actual: "actual", expected: "expected").addToSteps(testStep)
        bug.save()

        expect:
        bug.steps.size() == 1
        testStep.id != null
        bug.id != null

        when: "delete test step"
        bug.removeFromSteps(testStep)
        testStep.delete()
        sessionFactory.currentSession.flush()

        then: "bug is still found"
        Step.findById(testStep.id) == null
        def b = Bug.findById(bug.id)
        b.steps.size() == 0
    }
}
