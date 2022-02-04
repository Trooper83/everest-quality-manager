package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestCycle)
abstract class TestCycleService implements ITestCycleService {

    /**
     * removes an iteration from a test cycle
     * @param testCycle
     * @param testIteration
     */
    @Transactional
    void removeTestIteration(TestCycle testCycle, TestIteration testIteration) {
        testCycle.removeFromTestIterations(testIteration)
    }
}
