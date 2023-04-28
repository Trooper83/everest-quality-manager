package com.everlution

import com.everlution.command.RemovedItems
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestCase)
abstract class TestCaseService implements ITestCaseService {

    TestStepService testStepService

    /**
     * finds all test cases in a project with the name
     */
    @Transactional
    SearchResult findAllByProject(Project project, Map args) {
        int c = TestCase.countByProject(project)
        List tests = TestCase.findAllByProject(project, args)
        return new SearchResult(tests, c)
    }

    /**
     * finds all test cases in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllInProjectByName(Project project, String s, Map args) {
        List tests = TestCase.findAllByProjectAndNameIlike(project, "%${s}%", args)
        int c = TestCase.countByProjectAndNameIlike(project, "%${s}%")
        return new SearchResult(tests, c)
    }

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
