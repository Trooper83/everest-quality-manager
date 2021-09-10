package com.everlution

import grails.gorm.services.Service

@Service(TestCase)
abstract class TestCaseService implements ITestCaseService {

    void deleteAllTestCasesByProject(Project project) {
        def cases = TestCase.findAllByProject(project)
        cases.each {
            it.delete()
        }
    }

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
