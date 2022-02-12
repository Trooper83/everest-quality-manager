package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestCycle)
abstract class TestCycleService implements ITestCycleService {

    /**
     * adds test cases to a test cycle
     */
    @Transactional
    void addTestIterations(TestCycle testCycle, List<TestCase> testCases) {
        //TODO: filter out the tests by platform and environment
        def iterations = createIterations(testCases)
        iterations.each {
            testCycle.addToTestIterations(it)
        }
    }

    /**
     * removes an iteration from a test cycle
     */
    @Transactional
    void removeTestIteration(TestCycle testCycle, TestIteration testIteration) {
        testCycle.removeFromTestIterations(testIteration)
    }

    /**
     * creates test iterations from a list of test cases
     */
    private List<TestIteration> createIterations(List<TestCase> testCases) {
        def iterations = []
        testCases.each { TestCase testCase ->
            List<IterationStep> steps = []
            testCase.steps.each { Step step ->
                def s = new IterationStep(action: step.action, result: step.result)
                steps.add(s)
            }
            iterations.add(new TestIteration(name: testCase.name, result: "ToDo", steps: steps, testCase: testCase))
        }
        return iterations
    }
}
