package com.everlution

import grails.test.hibernate.HibernateSpec

class TestStepHibernateSpec extends HibernateSpec {

    void "test date created auto generated"() {
        when:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()

        then:
        testStep.dateCreated != null
    }

    void "delete step does not cascade to case"() {
        given: "valid domain instances"
        Project project = new Project(name: "TestStep Cascade Project", code: "TCP").save()
        TestStep testStep = new TestStep(action: "do something", result: "something happened")
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
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
        def tc = TestCase.findById(testCase.id)
        tc.steps.size() == 0
    }
}
