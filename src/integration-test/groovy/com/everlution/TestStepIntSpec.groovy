package com.everlution

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class TestStepIntSpec extends Specification {

    TestCaseService testCaseService

    void "test save case with steps"() {
        when:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()
        TestStep testStep1 = new TestStep(action: "do something", result: "something happened").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "automated", type: "ui", steps: [testStep, testStep1])
        testCaseService.save(testCase)

        then:
        TestStep.list().size == 2
    }

    void "test update case with steps"() {
        given:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "automated", type: "ui", steps: [testStep])
        testCaseService.save(testCase)

        when:
        testStep.action = "edited action"
        testStep.result = "edited result"

        then:
        testCase.steps[0].action == "edited action"
        testCase.steps[0].result == "edited result"
    }

    void "test delete case does not cascade to steps"() {
        given:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "automated", type: "ui", steps: [testStep])
        testCaseService.save(testCase)

        expect:
        testStep.get(testStep.id) != null

        when:
        testCaseService.delete(testCase.id)

        then:
        testCaseService.get(testCase.id) == null
        testStep.get(testStep.id) != null
    }

    void "test date created auto generated"() {
        when:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()

        then:
        testStep.dateCreated != null
    }

    void "test steps order persists"() {
        when:
        def paramMap = [creator: "test", name: "case", description: "desc",
                        executionMethod: "automated", type: "api",
                        "steps[0]": [action: "act1", result: "res1"],
                        "steps[1]": [action: "act2", result: "res2"],
                        "steps[2]": [action: "act3", result: "res3"]
        ]
        TestCase testCase = new TestCase(paramMap)
        testCaseService.save(testCase)

        def tc = testCaseService.get(testCase.id)

        then:
        tc.steps[0].action == "act1"
        tc.steps[1].action == "act2"
        tc.steps[2].action == "act3"
    }
}
