package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestIteration)
abstract class TestIterationService implements ITestIterationService {

    /**
     * creates test iterations from a list of test cases
     */
    @Transactional
    List<TestIteration> createIterations(List<TestCase> testCases) {
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
