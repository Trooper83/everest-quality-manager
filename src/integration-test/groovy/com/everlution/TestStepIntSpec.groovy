package com.everlution

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class TestStepIntSpec extends Specification {

    void "test save case with steps"() {
        when:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()
        TestStep testStep1 = new TestStep(action: "do something", result: "something happened").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "automated", type: "ui", steps: [testStep, testStep1]).save()

        then:
        testCase.steps.size() == 2
    }

    void "test date created auto generated"() {
        when:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()

        then:
        testStep.dateCreated != null
    }
}
