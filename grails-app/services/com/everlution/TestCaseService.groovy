package com.everlution

import com.everlution.command.RemovedItems
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestCase)
abstract class TestCaseService implements ITestCaseService {

    TestStepService testStepService

    /**
     * gets all test cases for the supplied ids
     */
    @Transactional
    List<TestCase> getAll(List<Serializable> ids) {
        return TestCase.getAll(ids)
    }

    /**
     * save an updated test case, deletes any removed steps
     * @param testCase - the test case to update
     * @param removedItems - ids of the steps to remove
     * @return - the updated test case
     */
    @Transactional
    TestCase saveUpdate(TestCase testCase, RemovedItems removedItems) {
        for(id in removedItems.stepIds) {
            def step = testStepService.get(id)
            testCase.removeFromSteps(step)
        }
        return save(testCase)
    }
}
