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
        if (!testCases) return
        def filteredTests = this.removeTestCases(testCycle, testCases)
        if (filteredTests.empty) return
        def iterations = this.createIterations(filteredTests)
        iterations.each {
            testCycle.addToTestIterations(it).save()
        }
        if (testCycle.testCaseIds) {
            testCycle.testCaseIds.addAll(filteredTests*.id)
        } else {
            testCycle.testCaseIds = filteredTests*.id
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
                def s = new IterationStep(action: step.act, result: step.result)
                steps.add(s)
            }
            iterations.add(new TestIteration(name: testCase.name, result: "ToDo", steps: steps, testCase: testCase))
        }
        return iterations
    }

    /**
     * removes test cases by the following criteria:
     * existing test cases already on the test cycle
     * test cases where the platform does not equal the testCycle.platform or null
     * test cases where the environment is not equal to testCycle.environ or null
     * @return list of filtered tests
     */
    private List<TestCase> removeTestCases(TestCycle testCycle, List<TestCase> testCases) {
        testCases.unique()
        if (testCycle.testCaseIds) {
            testCases.removeIf(test -> testCycle.testCaseIds.contains(test.id))
        }
        if (testCycle.platform != null) {
            testCases.removeIf(test -> (test.platform != testCycle.platform & test.platform != null))
        }
        if (testCycle.environ != null) {
            testCases.removeIf(test -> (!test.environments?.contains(testCycle.environ)) & test.environments != null)
        }
        return testCases
    }
}
