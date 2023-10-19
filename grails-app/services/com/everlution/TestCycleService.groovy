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
     * gets a test group with paginated test cases
     */
    @Transactional
    CycleWithPaginatedTests getWithPaginatedTests(Serializable id, Map params) {
        def cycle = get(id)
        List<TestIteration> tests = []
        if (cycle) {
            def c = cycle.testIterations?.size()
            if (c > 0) {
                def max = params.max as Integer
                def offset = params.offset as Integer
                max = max < 0 ? 0 : max
                offset = offset < 0 ? 0 : offset
                max = Math.min(max ?: 10, 100)
                offset = offset == null ? 0 : offset
                def start = offset == null ? 0 : offset
                def end = (start + max) > (c - 1) ? (c - 1) : (start + max) - 1
                if (start <= end) {
                    tests.addAll(cycle.testIterations.getAt(start..end))
                }
            }
        }
        return new CycleWithPaginatedTests(cycle, tests)
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
                def s = new IterationStep(act: step.act, result: step.result)
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
