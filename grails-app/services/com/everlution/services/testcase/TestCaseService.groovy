package com.everlution.services.testcase

import com.everlution.SearchResult
import com.everlution.services.step.StepService
import com.everlution.controllers.command.RemovedItems
import com.everlution.domains.Project
import com.everlution.domains.TestCase
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestCase)
abstract class TestCaseService implements ITestCaseService {

    StepService stepService

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
     * gets all tests with the supplied group
     */
    @Transactional
    List<TestCase> getAllByGroup(Serializable testGroupId, Map params) {
        def tests = TestCase.where {
            testGroups { id == testGroupId }
        }.list(params)
        return tests
    }

    /**
     * save an updated test case, deletes any removed free-form steps
     * @param testCase - the test case to update
     * @param removedItems - ids of the steps to remove
     * @return - the updated test case
     */
    @Transactional
    TestCase saveUpdate(TestCase testCase, RemovedItems removedItems) {
        def steps = []
        for(id in removedItems.stepIds) {
            def step = stepService.get(id)
            if (step) {
                steps.add(step)
                testCase.removeFromSteps(step)
            }
        }
        return save(testCase)
    }
}
