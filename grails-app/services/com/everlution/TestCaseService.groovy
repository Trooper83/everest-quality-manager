package com.everlution

import com.everlution.command.RemovedItems
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestCase)
abstract class TestCaseService implements ITestCaseService {

    StepService stepService

    /**
     * deletes a test case, removes (builder) or deletes (free-from) steps
     * @param id
     */
    @Transactional
    void delete(Serializable id) {
        def tc = get(id)
        if (tc) {
            List steps = tc.steps
            tc.delete()
            for (Step step in steps) {
                if (!step.isBuilderStep) {
                    stepService.delete(step.id)
                }
            }
        }
    }

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
        def updated = save(testCase)
        for (Step step in steps) {
            if (!step.isBuilderStep) {
                stepService.delete(step.id)
            }
        }
        return updated
    }
}
