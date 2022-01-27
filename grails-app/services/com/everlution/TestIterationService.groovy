package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestIteration)
abstract class TestIterationService implements ITestIterationService {

    /**
     * creates a test iteration from a test case
     * @param testCase
     * @return - the test iteration
     */
    @Transactional
    TestIteration createIterationFromTestCase(TestCase testCase) {
        def steps = []
        testCase.steps.each {
            def s = new IterationStep(action: it.action, result: it.result)
            steps.add(s)
        }
        new TestIteration(name: testCase.name, result: "ToDo", steps: steps, testCase: testCase)
    }
}
