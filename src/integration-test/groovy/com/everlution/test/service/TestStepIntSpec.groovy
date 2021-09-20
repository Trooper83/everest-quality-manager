package com.everlution.test.service

import com.everlution.Project
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestStep
import com.everlution.TestStepService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestStepIntSpec extends Specification {

    TestCaseService testCaseService
    SessionFactory sessionFactory
    TestStepService testStepService

    void "test save case with steps"() {
        when:
        Project project = new Project(name: "TestStep Save Project", code: "TPS")
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()
        TestStep testStep1 = new TestStep(action: "do something", result: "something happened").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep, testStep1], project: project)
        testCaseService.save(testCase)

        then:
        testCaseService.get(testCase.id).steps.size() == 2
    }

    void "test update case with steps"() {
        given:
        Project project = new Project(name: "TestStep Update Project", code: "TUP")
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep], project: project)
        testCaseService.save(testCase)

        when:
        testCase.steps[0].action = "edited action"
        testCase.steps[0].result = "edited result"

        then:
        def step = testStepService.get(testStep.id)
        step.action == "edited action"
        step.result == "edited result"
    }

    void "test delete case cascades to steps"() {
        given:
        Project project = new Project(name: "TestStep Cascade Project", code: "TCP")
        TestStep testStep = new TestStep(action: "do something", result: "something happened")
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "UI", project: project).addToSteps(testStep)
        testCaseService.save(testCase)

        expect:
        testStepService.get(testStep.id) != null

        when:
        testCaseService.delete(testCase.id)
        sessionFactory.currentSession.flush()

        then:
        testCaseService.get(testCase.id) == null
        testStepService.get(testStep.id) == null
    }

    void "test steps order persists"() {
        when:
        Project project = new Project(name: "TestStep Step Order Project", code: "SOP")
        def paramMap = [creator: "test", name: "case", description: "desc",
                        executionMethod: "Automated", type: "API",
                        "steps[0]": [action: "act1", result: "res1"],
                        "steps[1]": [action: "act2", result: "res2"],
                        "steps[2]": [action: "act3", result: "res3"],
                        project: project
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
