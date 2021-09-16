package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestCase)
abstract class TestCaseService implements ITestCaseService {

    @Transactional
    void deleteAllTestCasesByProject(Project project) {
        def cases = TestCase.findAllByProject(project)
        cases.each {
            it.delete()
        }
    }

    @Transactional
    @Override
    void delete(Serializable id) {
        def testCase = get(id)
        def stepsList = []
        if(testCase.steps) {
            stepsList += testCase.steps
        }
        stepsList.each {
            testCase.removeFromSteps(it)
            it.delete()
        }
        testCase.delete()
    }
}
