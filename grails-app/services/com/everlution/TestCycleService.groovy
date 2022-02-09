package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestCycle)
abstract class TestCycleService implements ITestCycleService {

    TestIterationService testIterationService

    /**
     * adds test cases to a test cycle
     */
    @Transactional
    void addTestIterations(TestCycle testCycle, List<TestCase> testCases) {
        def iterations = testIterationService.createIterations(testCases)
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
}
