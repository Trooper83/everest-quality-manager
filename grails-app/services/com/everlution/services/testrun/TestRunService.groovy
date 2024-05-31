package com.everlution.services.testrun

import com.everlution.RunWithPaginatedResults
import com.everlution.SearchResult
import com.everlution.TestRunResult
import com.everlution.domains.AutomatedTest
import com.everlution.domains.Project
import com.everlution.domains.TestResult
import com.everlution.domains.TestRun
import com.everlution.services.automatedtest.AutomatedTestService
import com.everlution.services.testresult.TestResultService
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException

@Service(TestRun)
abstract class TestRunService implements ITestRunService {

    AutomatedTestService automatedTestService
    TestResultService testResultService

    @Transactional
    SearchResult findAllByProject(Project project, Map args) {
        int c = TestRun.countByProject(project)
        List t = TestRun.findAllByProject(project, args)
        return new SearchResult(t, c)
    }

    @Transactional
    SearchResult findAllInProjectByName(Project project, String name, Map args) {
        int c = TestRun.countByProjectAndNameIlike(project, "%${name}%")
        List t = TestRun.findAllByProjectAndNameIlike(project, "%${name}%", args)
        return new SearchResult(t, c)
    }

    @Transactional
    TestRun save(TestRun testRun) {
        if(!testRun.validate()) {
            throw new ValidationException("TestRun failed to validate", testRun.errors)
        }
        testRun.save(flush: true)
    }

    @Transactional
    TestRun createAndSave(String name, Project project, List<TestRunResult> results) {
        List<TestResult> testResults = []
        List<AutomatedTest> tests = []
        for(TestRunResult r in results) {
            // if test was already found or created use that and add result to it
            def test = tests.find { it -> it.fullName == r.testName }
            if(test == null) {
                test = automatedTestService.findOrSave(project, r.testName)
                tests.add(test)
            }
            // set failureCause to null if exceeding limit
            if(r.failureCause?.length() > 2500) {
                r.failureCause = null
            }
            def tr = new TestResult(automatedTest: test, result: r.result.toUpperCase(), failureCause: r.failureCause)
            testResults.add(tr)
        }
        save(new TestRun(name: name, project: project, testResults: testResults))
    }

    @Transactional
    RunWithPaginatedResults getWithPaginatedResults(Serializable id, Map params) {
        def run = get(id)
        List<TestResult> resultsList = []
        if (run) {
            def results = testResultService.findAllByTestRun(run, params)
            resultsList.addAll(results)
        }
        return new RunWithPaginatedResults(run, resultsList)
    }
}
