package com.everlution

import grails.gorm.services.Service
import grails.validation.ValidationException

@Service(TestResult)
abstract class TestResultService implements ITestRunService {

    AutomatedTestService automatedTestService

    List<TestResult> createAndSave(Project project, List<TestRunResult> results) throws ValidationException {
        List testResults = []
        results?.forEach( r -> {
            def test = automatedTestService.findOrSave(project, r.testName)
            def tr = new TestResult(automatedTest: test, result: r.result)
            testResults.add(save(tr))
        })
        return testResults
    }
}
